package com.gendb.model;

public class Column {

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
}
