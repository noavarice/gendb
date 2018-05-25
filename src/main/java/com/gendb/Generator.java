package com.gendb;

import com.gendb.dto.DatabaseDto;
import com.gendb.dto.ObjectFactory;
import com.gendb.generation.InternalGenerator;
import com.gendb.generation.RandomValueProvider;
import com.gendb.mapper.PureModelMapper;
import com.gendb.mapper.ValidationModelMapper;
import com.gendb.model.pure.Database;
import com.gendb.model.pure.Table;
import com.gendb.model.validating.ValidatingDatabase;
import com.gendb.util.MapperUtils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Set;
import java.util.StringJoiner;
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
  private static ValidatingDatabase getConfig(final InputStream input) throws JAXBException {
    final Unmarshaller unmarshaller = createUnmarshaller();
    final ValidationModelMapper mapper = MapperUtils.getMapper(ValidationModelMapper.class);
    final JAXBElement<DatabaseDto> element = (JAXBElement<DatabaseDto>)unmarshaller.unmarshal(input);
    return mapper.toValidationModel(element.getValue());
  }

  private final Path configPath;

  private final Validator validator;

  private RandomValueProvider random;

  public Generator(final Path configPath, final Validator validator) {
    this.configPath = configPath;
    this.validator = validator;
    this.random = new RandomValueProvider();
  }

  public void setRandomnessProvider(final RandomValueProvider provider) {
    this.random = provider;
  }

  private static final int MAX_ROWS_IN_LINE = 10;

  private static void insertLine(final InternalGenerator generator, final OutputStream output, final int count)
    throws IOException {
    final StringJoiner rowJoiner = new StringJoiner(",", System.lineSeparator(), "");
    for (int i = 0; i < count; ++i) {
      final StringJoiner columnJoiner = new StringJoiner(",", "(", ")");
      generator.getRow().forEach(wrapper -> columnJoiner.add(wrapper.queryRepresentation()));
      rowJoiner.add(columnJoiner.toString());
    }

    output.write(rowJoiner.toString().getBytes());
  }

  private void writeToStream(final Database db, final OutputStream output) throws IOException {
    output.write(db.getCreateStatement().getBytes());
    for (final Table t : db.getTables()) {
      LOGGER.info("Start generating table '{}'", t.getName());
      final String createTable = t.getCreateStatement();
      final String foreignKeys = t.getForeignKeyDeclarations();
      final String insertStatement = t.getInsertStatement();
      output.write((createTable + foreignKeys + insertStatement).getBytes());
      final InternalGenerator generator = new InternalGenerator(t, random);
      final boolean lastLineComplete = t.getRowsCount() % MAX_ROWS_IN_LINE == 0;
      final int lastLineRowCount, completeLinesCount;
      if (lastLineComplete) {
        lastLineRowCount = MAX_ROWS_IN_LINE;
        completeLinesCount = t.getRowsCount() / MAX_ROWS_IN_LINE - 1;
      } else {
        lastLineRowCount = t.getRowsCount() % MAX_ROWS_IN_LINE;
        completeLinesCount = t.getRowsCount() / MAX_ROWS_IN_LINE;
      }

      for (int i = 0; i < completeLinesCount; ++i) {
        insertLine(generator, output, MAX_ROWS_IN_LINE);
        output.write(',');
      }

      insertLine(generator, output, lastLineRowCount);
      output.write((";\n" + System.lineSeparator()).getBytes());
      LOGGER.info("Finish generating table '{}'", t.getName());
    }
  }

  public void createScript(final Path scriptFilePath, final boolean override) throws IOException {
    if (Files.exists(scriptFilePath, LinkOption.NOFOLLOW_LINKS)) {
      if (!override) {
        LOGGER.error("File '{}' already exists, override: {}", scriptFilePath, false);
        return;
      }

      LOGGER.warn("File '{}' already exists, override: {}", scriptFilePath, true);
    }

    final FileInputStream input = new FileInputStream(configPath.toFile());
    final ValidatingDatabase validationDatabase;
    try {
      validationDatabase = getConfig(input);
    } catch (JAXBException e) {
      LOGGER.error("Error while parsing XML config:\n{}", e.toString());
      return;
    }

    final Set<ConstraintViolation<ValidatingDatabase>> violations = validator.validate(validationDatabase);
    if (!violations.isEmpty()) {
      LOGGER.error("Constraint violations are found");
      violations.stream().map(ConstraintViolation::getMessage).forEach(LOGGER::error);
      return;
    }

    final Database database = MapperUtils.getMapper(PureModelMapper.class).toModel(validationDatabase);
    final FileOutputStream output = new FileOutputStream(scriptFilePath.toFile());
    writeToStream(database, output);
  }
}
