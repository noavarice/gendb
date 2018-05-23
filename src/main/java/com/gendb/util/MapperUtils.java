package com.gendb.util;

import java.util.ServiceLoader;

public class MapperUtils {

  public static <T> T getMapper(final Class<T> clazz) {
    final ServiceLoader<T> mapperServiceLoader = ServiceLoader.load(clazz);
    return mapperServiceLoader.iterator().next();
  }
}
