package com.gendb.model;

import com.gendb.validation.Violations;
import com.gendb.validation.type.DecimalPropertiesPresent;
import com.gendb.validation.type.HandlerClassExist;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@DecimalPropertiesPresent
public class DataType {

  private String name;

  private boolean nullable;

  private String defaultValue;

  @HandlerClassExist
  private String handlerClass;

  @Positive(message = Violations.NON_POSITIVE_PRECISION)
  private Integer precision;

  @PositiveOrZero(message = Violations.NEGATIVE_PRECISION)
  private Integer scale;

  @Positive
  private int length;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getPrecision() {
    return precision;
  }

  public void setPrecision(Integer precision) {
    this.precision = precision;
  }

  public Integer getScale() {
    return scale;
  }

  public void setScale(Integer scale) {
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

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public String getHandlerClass() {
    return handlerClass;
  }

  public void setHandlerClass(String handlerClass) {
    this.handlerClass = handlerClass;
  }
}
