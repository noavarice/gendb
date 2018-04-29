package com.gendb;

import com.gendb.dto.DatabaseDto;
import com.gendb.dto.ObjectFactory;
import com.gendb.mapper.ModelMapper;
import com.gendb.model.Database;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ServiceLoader;
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

public final class Generator {

  private static Schema loadConfigSchema() throws SAXException {
    final Source schemaSource = new StreamSource(ClassLoader.getSystemResourceAsStream("config-schema.xsd"));
    final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    return factory.newSchema(schemaSource);
  }

  public static void fromStream(final InputStream input, final OutputStream output)
      throws JAXBException, SAXException {
    final JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
    final Schema configSchema = loadConfigSchema();
    final Unmarshaller unmarshaller = context.createUnmarshaller();
    unmarshaller.setSchema(configSchema);
    final ServiceLoader<ModelMapper> mapperServiceLoader = ServiceLoader.load(ModelMapper.class);
    final ModelMapper mapper = mapperServiceLoader.iterator().next();
    final JAXBElement<DatabaseDto> element = (JAXBElement<DatabaseDto>)unmarshaller.unmarshal(input);
    final Database d = mapper.toModel(element.getValue());
    System.out.println(d.toString());
  }
}
