package com.gendb;

import com.gendb.dto.DatabaseDto;
import com.gendb.dto.ObjectFactory;
import com.gendb.exception.ConfigReadingException;
import com.gendb.exception.ConnectionException;
import com.gendb.exception.GenerationException;
import com.gendb.exception.ScriptGenerationException;
import com.gendb.exception.ValidationException;
import com.gendb.generation.InternalGenerator;
import com.gendb.generation.RandomProvider;
import com.gendb.mapper.PureModelMapper;
import com.gendb.mapper.ValidationModelMapper;
import com.gendb.model.pure.Database;
import com.gendb.model.pure.Table;
import com.gendb.model.validating.ValidatingDatabase;
import com.gendb.model.wrapper.ValueWrapper;
import com.gendb.util.MapperUtils;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * This class provides API for using data generation.
 *
 * Current class is an entry point to use library functionality.
 * Public interface represented with two methods:
 * <ul>
 *   <li>{@link this#createScript(InputStream, OutputStream)};</li>
 *   <li>{@link this#createDatabase(InputStream, Properties)}.</li>
 * </ul>
 */
public final class Generator {

  private static final Logger LOGGER = LoggerFactory.getLogger(Generator.class);

  /**
   * Loads local XSD resource that describes XML configuration file structure
   * and is using to validate input file
   *
   * @return {@link Schema} create from loaded XSD file
   */
  private static Schema loadConfigSchema() {
    final Source schemaSource = new StreamSource(ClassLoader.getSystemResourceAsStream("config-schema.xsd"));
    final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    try {
      return factory.newSchema(schemaSource);
    } catch (SAXException e) {
      // this clause should not be ever reached
      assert false;
    }

    return null;
  }

  /**
   * Performs necessary actions to create new {@link Unmarshaller} instance
   *
   * @return If instantiation was successful, returns newly created unmarshaller
   * @throws JAXBException in the case unmarshaller instantiation has failed
   * due to its internal reasons
   */
  private static Unmarshaller createUnmarshaller() throws JAXBException {
    final Schema configSchema = loadConfigSchema();
    final JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
    final Unmarshaller unmarshaller = context.createUnmarshaller();
    unmarshaller.setSchema(configSchema);
    return unmarshaller;
  }

  /**
   * Performs actual unmarshalling process upon passed XML config file
   * @param input XML config file as {@link InputStream}
   */
  private static ValidatingDatabase getConfig(final InputStream input) throws ConfigReadingException {
    final Unmarshaller unmarshaller;
    final JAXBElement<DatabaseDto> element;
    try {
      unmarshaller = createUnmarshaller();
      element = (JAXBElement<DatabaseDto>)unmarshaller.unmarshal(input);
    } catch (JAXBException e) {
      throw new ConfigReadingException("Failed to get data from configuration file", e, false, true);
    }

    final ValidationModelMapper mapper = MapperUtils.getMapper(ValidationModelMapper.class);
    return mapper.toValidationModel(element.getValue());
  }

  private final Validator validator;

  private RandomProvider random;

  public Generator(final Validator validator) {
    this.validator = validator;
    this.random = new RandomProvider();
  }

  public void setRandomnessProvider(final RandomProvider provider) {
    this.random = provider;
  }

  /**
   * Creates a string representing values for INSERT statement
   * of particular {@code count} of rows
   *
   * @param generator Object which generates data
   * @param count Count of rows to be generated
   * @return Result of concatenation of generated rows. Values in a row are separated
   * by commas, each row is enclosed with brackets. Example (for {@code count} of 3:
   * "(1, 'test', 2010-01-01), (2, 'test2', 2010-01-02), (3 'test3', 2010-01-03)"
   */
  private static String getBatches(final InternalGenerator generator, final int count) {
    final StringJoiner rowJoiner = new StringJoiner(",", System.lineSeparator(), ";");
    for (int i = 0; i < count; ++i) {
      final StringJoiner columnJoiner = new StringJoiner(",", "(", ")");
      generator.getRow().forEach(wrapper -> columnJoiner.add(wrapper.queryRepresentation()));
      rowJoiner.add(columnJoiner.toString());
    }

    return rowJoiner.toString();
  }

  /**
   * Helper method that writes {@code args} to an {@code output} stream in form of strings
   *
   * If no {@code args} were passed, does nothing.
   * Null object from {@code args} will be represented as "null" (default behaviour
   * of {@link String#valueOf(Object)} method)
   *
   * @param output Where to write data
   * @param args Array of object to be written to stream. Each object will be written
   * as a result of calling {@link String#valueOf} method on it
   * @throws ScriptGenerationException Thrown in case of problems with actual writing
   * to {@code output}
   */
  private static void write(final OutputStream output, final Object... args) throws ScriptGenerationException {
    if (args == null || args.length == 0) {
      return;
    }

    final String summary = Arrays
        .stream(args)
        .map(String::valueOf)
        .reduce("", String::concat);
    try {
      output.write(summary.getBytes());
    } catch (IOException e) {
      throw new ScriptGenerationException("Failed to perform script file writing", e, false, true);
    }
  }

  /**
   * Performs SQL script creation. Internally uses {@link this#write(OutputStream, Object...)}
   *
   * @param db Object containing information about database to be generated
   * and saved as SQL script
   * @param output Where to write generated data in form of SQL queries.
   * This might be a {@link FileOutputStream} or some other subclass of {@link OutputStream}
   * @throws GenerationException Thrown in case of encountering problems with writing data
   * to {@code output} stream
   */
  private void writeToStream(final Database db, final OutputStream output) throws GenerationException {
    final int batchSize = db.getBatchSize();
    for (final Table t : db.getTables()) {
      LOGGER.info("Start generating table '{}'", t.getName());
      final String createTable = t.getCreateStatement();
      final String foreignKeys = t.getForeignKeyDeclarations();
      write(output, createTable, foreignKeys);
      final InternalGenerator generator = InternalGenerator.createGenerator(t, random);
      final int fullBatchesCount = t.getRowsCount() / batchSize;
      final String insertStatement = t.getInsertStatement();
      for (int i = 0; i < fullBatchesCount; ++i) {
        final String batch = getBatches(generator, batchSize);
        write(output, insertStatement, batch);
      }

      final int lastBatchSize = t.getRowsCount() % batchSize;
      if (lastBatchSize > 0) {
        write(output, insertStatement, getBatches(generator, lastBatchSize), System.lineSeparator());
      }

      LOGGER.info("Finish generating table '{}'", t.getName());
    }
  }

  /**
   * Analog of {@link this#write(OutputStream, Object...)} method to write
   * several generated rows to database through {@code insert} statement
   *
   * @param insert Statement used to write data to database
   * @param generator Object generating the data
   * @param batchCount Amount of rows to be generated
   * @throws SQLException Thrown in case of problems while using passed {@code insert} statement
   */
  private static void write(
      final PreparedStatement insert,
      final InternalGenerator generator,
      final int batchCount) throws SQLException {
    for (int batchNumber = 0; batchNumber < batchCount; ++batchNumber) {
      final List<Object> row = generator.getRow().stream()
          .map(ValueWrapper::plain)
          .collect(Collectors.toList());
      for (int colNumber = 1; colNumber <= row.size(); ++colNumber) {
        insert.setObject(colNumber, row.get(colNumber - 1));
      }

      insert.addBatch();
    }

    insert.executeBatch();
  }

  /**
   * Creates database and saves it in DBMS
   *
   * @param dbConfig Object containing information about database to be generated
   * @param connection Represents connection with DBMS.
   * It is implied that database (or schema) is created (and selected in case of MYSQL)
   * and necessary modes are set
   * @throws SQLException Thrown in case of problems while working with {@code connection}
   * @throws GenerationException Various internal generation problems
   */
  private void writeToConnection(final Database dbConfig, final Connection connection)
      throws SQLException, GenerationException {
    final Statement dmlStatement = connection.createStatement();
    final int batchSize = dbConfig.getBatchSize();
    for (final Table t: dbConfig.getTables()) {
      LOGGER.info("Start generating table '{}'", t.getName());
      dmlStatement.execute(t.getCreateStatement());
      final String template = t.getInsertStatementForConnection();
      final PreparedStatement insertStatement = connection.prepareStatement(template);
      final InternalGenerator generator = InternalGenerator.createGenerator(t, random);
      final int fullBatchesCount = t.getRowsCount() / batchSize;
      for (int i = 0; i < fullBatchesCount; ++i) {
        write(insertStatement, generator, batchSize);
      }

      final int lastBatchSize = t.getRowsCount() % batchSize;
      write(insertStatement, generator, lastBatchSize);
      insertStatement.close();
      LOGGER.info("Finish generating table '{}'", t.getName());
    }

    dmlStatement.close();
  }

  /**
   * Performs validation upon database configuration object
   *
   * @param dbConfig Configuration object to be validated
   * @throws ValidationException Thrown if constraint violations are found
   */
  private void validate(final ValidatingDatabase dbConfig) throws ValidationException {
    final Set<ConstraintViolation<ValidatingDatabase>> violations = validator.validate(dbConfig);
    if (violations.isEmpty()) {
      return;
    }

    final StringJoiner joiner = new StringJoiner(System.lineSeparator());
    joiner.add("Constraint violations are found");
    violations.stream().map(ConstraintViolation::getMessage).forEach(joiner::add);
    throw new ValidationException(joiner.toString());
  }

  /**
   * Method responsible for database generation and saving the result in form of SQL queries
   * to passed {@code output} stream
   *
   * @param input Stream of XML configuration data containing database structure
   * @param output Where to write generating database
   * @throws GenerationException Any problems while generating and writing data are wrapped
   * to application-specific exceptions all of which are subclassing {@link GenerationException}
   */
  public void createScript(final InputStream input, final OutputStream output) throws GenerationException {
    final ValidatingDatabase dbConfig = getConfig(input);
    validate(dbConfig);
    final Database pureConfig = MapperUtils.getMapper(PureModelMapper.class).toModel(dbConfig);
    writeToStream(pureConfig, output);
  }

  /**
   * Method responsible for database generation and saving the result to database
   * through DBMS connection
   *
   * @param input Stream of XML configuration data containing database structure
   * @param connProps Properties to connect to DBMS (such as URL, user, password, etc.)
   * @throws GenerationException Any problems while generating and writing data are wrapped
   * to application-specific exceptions all of which are subclassing {@link GenerationException}
   */
  public void createDatabase(final InputStream input, final Properties connProps) throws GenerationException {
    final ValidatingDatabase dbConfig = getConfig(input);
    validate(dbConfig);
    final Database pureConfig = MapperUtils.getMapper(PureModelMapper.class).toModel(dbConfig);
    try {
      final String connUrl = connProps.getProperty("url");
      final Connection dbConnection = DriverManager.getConnection(connUrl, connProps);
      writeToConnection(pureConfig, dbConnection);
      dbConnection.close();
    } catch (SQLException e) {
      throw new ConnectionException("Failed to create database", e, false, true);
    }
  }
}
