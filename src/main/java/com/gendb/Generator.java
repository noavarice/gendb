package com.gendb;

import com.gendb.dto.DatabaseDto;
import com.gendb.mapper.ModelMapper;
import com.gendb.model.Database;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ServiceLoader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public final class Generator {

  public static void fromStream(final InputStream input, final OutputStream output) throws JAXBException{
    if (!GeneratorUtils.isValidStructure(input)) {
      System.out.println("Not valid");
      return;
    }

    final JAXBContext context = JAXBContext.newInstance(Database.class);
    final Unmarshaller unmarshaller = context.createUnmarshaller();
    final ServiceLoader<ModelMapper> mapperServiceLoader = ServiceLoader.load(ModelMapper.class);
    final ModelMapper mapper = mapperServiceLoader.iterator().next();
    final Database d = mapper.toModel((DatabaseDto)(unmarshaller.unmarshal(input)));
    System.out.println(d.toString());
  }
}
