package com.gendb.model;

import javax.validation.Valid;

public class Column {

  @Valid
  private DataType type;

  private String name;

  public DataType getType() {
    return type;
  }

  public void setType(DataType type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getColumnDefinition() {
    return name + ' ' + type.getTypeDefinition();
  }
}
