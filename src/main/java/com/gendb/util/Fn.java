package com.gendb.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Fn {

  public static <T, R> Collection<R> map(final Collection<T> collection, final Function<T, R> f) {
    if (collection == null || collection.isEmpty()) {
      return new ArrayList<>();
    }

    if (f == null) {
      throw new IllegalArgumentException("Mapper function cannot be null");
    }

    return collection.stream().map(f).collect(Collectors.toList());
  }
}
