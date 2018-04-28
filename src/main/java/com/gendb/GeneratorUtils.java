package com.gendb;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

class GeneratorUtils {

  private static String SCHEMA_PATH = "config-schema.xsd";

  static boolean isValidStructure(final InputStream is) {
    final Source xmlFile = new StreamSource(is);
    final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    try (final InputStream schemaData = ClassLoader.getSystemResourceAsStream(SCHEMA_PATH)) {
      try {
        final Schema schema = schemaFactory.newSchema(new StreamSource(schemaData));
        Validator validator = schema.newValidator();
        validator.validate(xmlFile);
      } catch (IOException | SAXException e) {
        System.out.println(e.toString());
        return false;
      }
    } catch (IOException e) {
      System.out.println(e.toString());
      return false;
    }

    return true;
  }
}
