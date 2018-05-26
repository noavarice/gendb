package com.gendb.model.pure;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum SupportedDbms {

  MYSQL("mysql"),
  POSTGRESQL("postgresql"),
  ;

  private static final Map<String, SupportedDbms> NAME_TO_VALUE = Arrays.stream(SupportedDbms.values())
      .collect(Collectors.toMap(SupportedDbms::getName, Function.identity()));

  private final String name;

  SupportedDbms(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static SupportedDbms fromName(final String name) {
    return NAME_TO_VALUE.get(name);
  }
}
