package com.gendb;

import com.gendb.dto.DatabaseDto;
import com.gendb.dto.ObjectFactory;
import com.gendb.mapper.ModelMapper;
import com.gendb.model.Database;
import com.gendb.model.Table;
import com.gendb.model.wrapper.DefaultWrapper;
import com.gendb.random.RandomValueProvider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
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
import org.xml.sax.SAXException;

/**
 * Main class for SQL generating
 */
public final class Generator {

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
   * Retrieves {@link ModelMapper} implementation through {@link ServiceLoader}
   */
  private static ModelMapper createModelMapper() {
    final ServiceLoader<ModelMapper> mapperServiceLoader = ServiceLoader.load(ModelMapper.class);
    return mapperServiceLoader.iterator().next();
  }

  /**
   * Performs actual unmarshalling process upon passed XML config file
   * @param input XML config file as {@link InputStream}
   */
  private static Database getConfig(final InputStream input) throws JAXBException {
    final Unmarshaller unmarshaller = createUnmarshaller();
    final ModelMapper mapper = createModelMapper();
    final JAXBElement<DatabaseDto> element = (JAXBElement<DatabaseDto>)unmarshaller.unmarshal(input);
    return mapper.toModel(element.getValue());
  }

  private final Validator validator;

  private final RandomValueProvider random;

  public Generator(final Validator validator) {
    this.validator = validator;
    this.random = new RandomValueProvider();
  }

  public Generator(final Validator validator, final RandomValueProvider random) {
    this.validator = validator;
    this.random = random;
  }

  private static final int MAX_ROWS_IN_LINE = 10;

  private void writeToStream(final Database db, final OutputStream output) throws IOException {
    output.write(db.getCreateStatement().getBytes());
    for (final Table t : db.getTables()) {
      output.write((t.getCreateStatement() + t.getInsertStatement()).getBytes());
      final Iterator<List<DefaultWrapper>> rowIterator = t.getValuesIterator(random);
      StringJoiner rowJoiner = new StringJoiner(",");
      int rowsInLine = 0;
      if (rowIterator.hasNext()) {
        final List<DefaultWrapper> row = rowIterator.next();
        final StringJoiner columnJoiner = new StringJoiner(",", "(", ")");
        row.forEach(wrapper -> columnJoiner.add(wrapper.processed()));
        output.write(columnJoiner.toString().getBytes());
        ++rowsInLine;
      }

      while (rowIterator.hasNext()) {
        final List<DefaultWrapper> row = rowIterator.next();
        final StringJoiner columnJoiner = new StringJoiner(",", "(", ")");
        row.forEach(wrapper -> columnJoiner.add(wrapper.processed()));
        rowJoiner.add(columnJoiner.toString());
        if (++rowsInLine == MAX_ROWS_IN_LINE) {
          rowsInLine = 0;
          output.write((",\n" + rowJoiner.toString()).getBytes());
          rowJoiner = new StringJoiner(",");
        }
      }

      output.write((rowJoiner.toString() + ';').getBytes());
    }
  }

  public void fromStream(final InputStream input, final OutputStream output) throws IOException {
    final Database db;
    try {
      db = getConfig(input);
    } catch (JAXBException e) {
      final String message = String.format("Errors while parsing XML config:\n%1$s", e.toString());
      System.out.println(message);
      return;
    }

    final Set<ConstraintViolation<Database>> violations = validator.validate(db);
    if (!violations.isEmpty()) {
      violations.stream().map(ConstraintViolation::getMessage).forEach(System.out::println);
      return;
    }

    writeToStream(db, output);
  }
}
