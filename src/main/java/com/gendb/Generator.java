package com.gendb;

import com.gendb.dto.DatabaseDto;
import com.gendb.dto.ObjectFactory;
import com.gendb.exception.ConfigReadingException;
import com.gendb.exception.ConnectionException;
import com.gendb.exception.GenerationException;
import com.gendb.exception.IncorrectTypeException;
import com.gendb.exception.ScriptGenerationException;
import com.gendb.exception.ValidationException;
import com.gendb.generation.InternalGenerator;
import com.gendb.generation.RandomProvider;
import com.gendb.mapper.PureModelMapper;
import com.gendb.mapper.ValidationModelMapper;
import com.gendb.model.pure.Database;
import com.gendb.model.pure.SupportedDbms;
import com.gendb.model.pure.Table;
import com.gendb.model.validating.ValidatingDatabase;
import com.gendb.model.wrapper.ValueWrapper;
import com.gendb.util.MapperUtils;
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
 * Main class for SQL generating
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

  private static String getBatch(final InternalGenerator generator, final int count) {
    final StringJoiner rowJoiner = new StringJoiner(",", System.lineSeparator(), ";");
    for (int i = 0; i < count; ++i) {
      final StringJoiner columnJoiner = new StringJoiner(",", "(", ")");
      generator.getRow().forEach(wrapper -> columnJoiner.add(wrapper.queryRepresentation()));
      rowJoiner.add(columnJoiner.toString());
    }

    return rowJoiner.toString();
  }

  private static void write(final OutputStream output, final Object... args) throws ScriptGenerationException {
    if (args == null) {
      return;
    }

    final String summary = Arrays.stream(args).map(Object::toString).reduce("", String::concat);
    try {
      output.write(summary.getBytes());
    } catch (IOException e) {
      throw new ScriptGenerationException("Failed to perform script file writing", e, false, true);
    }
  }

  private void writeToStream(final Database db, final OutputStream output) throws GenerationException {
    write(output, db.getCreateStatement());
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
        final String batch = getBatch(generator, batchSize);
        write(output, insertStatement, batch);
      }

      final int lastBatchSize = t.getRowsCount() % batchSize;
      if (lastBatchSize > 0) {
        write(output, insertStatement, getBatch(generator, lastBatchSize), System.lineSeparator());
      }

      LOGGER.info("Finish generating table '{}'", t.getName());
    }
  }

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

  private void writeToConnection(final Database dbConfig, final Connection connection)
      throws SQLException, GenerationException {
    final Statement dmlStatement = connection.createStatement();
    dmlStatement.execute(dbConfig.getCreateStatement());
    dmlStatement.execute(dbConfig.getConnectStatement());
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

  public void createScript(final InputStream input, final OutputStream output) throws GenerationException {
    final ValidatingDatabase dbConfig = getConfig(input);
    validate(dbConfig);
    final Database pureConfig = MapperUtils.getMapper(PureModelMapper.class).toModel(dbConfig);
    writeToStream(pureConfig, output);
  }

  public void createDatabase(final InputStream input, final Properties connProps) throws GenerationException {
    final ValidatingDatabase dbConfig = getConfig(input);
    validate(dbConfig);
    final Database pureConfig = MapperUtils.getMapper(PureModelMapper.class).toModel(dbConfig);
    try {
      final String connUrl = connProps.getProperty("url");
      final Connection connection = DriverManager.getConnection(connUrl, connProps);
      writeToConnection(pureConfig, connection);
    } catch (SQLException e) {
      throw new ConnectionException("Failed to create database", e, false, true);
    }
  }
}
