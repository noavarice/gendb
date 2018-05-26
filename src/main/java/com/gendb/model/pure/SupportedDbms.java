package com.gendb.model.pure;

public enum SupportedDbms {

  MYSQL("mysql"),
  POSTGRESQL("postgresql"),
  ;

  private final String name;

  SupportedDbms(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
