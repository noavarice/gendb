package com.gendb.model;

public class DataType {

  private String name;

  private boolean nullable;

  private String defaultValue;

  private int precision;

  private int scale;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPrecision() {
    return precision;
  }

  public void setPrecision(int precision) {
    this.precision = precision;
  }

  public int getScale() {
    return scale;
  }

  public void setScale(int scale) {
    this.scale = scale;
  }

  public boolean isNullable() {
    return nullable;
  }

  public void setNullable(boolean nullable) {
    this.nullable = nullable;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }
}
