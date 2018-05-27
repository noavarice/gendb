package com.gendb.util;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Types {

  INT("int"),
  SMALLINT("smallint"),
  FLOAT("real"),
  DOUBLE("double"),
  DECIMAL("decimal"),
  CHAR("char"),
  VARCHAR("varchar"),
  TIMESTAMP("timestamp"),
  DATE("date"),
  ;

  private static final Map<String, Types> NAME_TO_VALUE = Arrays.stream(Types.values())
      .collect(Collectors.toMap(Types::getName, Function.identity()));

  private final String name;

  Types(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getActualName() {
    if (!"double".equals(name)) {
      return name;
    }

    return "double precision";
  }

  public static Types fromName(final String name) {
    if (!NAME_TO_VALUE.containsKey(name)) {
      throw new IllegalArgumentException("Unknown type name: " + name);
    }

    return NAME_TO_VALUE.get(name);
  }
}
