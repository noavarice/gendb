package com.gendb.util;

public enum Types {

  INT("int"),
  SMALLINT("smallint"),
  DECIMAL("decimal"),
  CHAR("char"),
  VARCHAR("varchar"),
  ;

  private final String name;

  Types(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
