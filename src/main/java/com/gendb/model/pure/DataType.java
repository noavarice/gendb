package com.gendb.model.pure;

import com.gendb.util.TypeUtils;
import com.gendb.util.Types;

/**
 * Model representing data type for single SQL column.
 */
public class DataType {

  private String name;

  private boolean nullable;

  private String handlerClass;

  private Integer precision;

  private Integer scale;

  private Integer length;

  private Integer minLength;

  private Integer maxLength;

  private Double min;

  private Double max;

  private Column minColumn;

  private String dictionary;

  private boolean sequential;

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

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public String getHandlerClass() {
    return handlerClass;
  }

  public void setHandlerClass(String handlerClass) {
    this.handlerClass = handlerClass;
  }

  String getTypeDefinition() {
    final StringBuilder sb = new StringBuilder(Types.fromName(name).getActualName().toUpperCase());
    if (name.equals("decimal")) {
      sb.append(String.format("(%1$s,%2$s)", precision, scale));
    } else if (TypeUtils.isString(name)) {
      sb.append(String.format("(%1$s)", length != null ? length : maxLength));
    }

    if (!nullable) {
      sb.append(" NOT NULL");
    }

    if (name.equals("timestamp")) {
      sb.append(" DEFAULT CURRENT_TIMESTAMP");
    }

    return sb.toString();
  }

  public Double getMin() {
    return min;
  }

  public void setMin(Double min) {
    this.min = min;
  }

  public Double getMax() {
    return max;
  }

  public void setMax(Double max) {
    this.max = max;
  }

  public Column getMinColumn() {
    return minColumn;
  }

  public void setMinColumn(Column minColumn) {
    this.minColumn = minColumn;
  }

  public String getDictionary() {
    return dictionary;
  }

  public void setDictionary(String dictionary) {
    this.dictionary = dictionary;
  }

  public boolean isSequential() {
    return sequential;
  }

  public void setSequential(boolean sequential) {
    this.sequential = sequential;
  }

  public Integer getMinLength() {
    return minLength;
  }

  public void setMinLength(Integer minLength) {
    this.minLength = minLength;
  }

  public Integer getMaxLength() {
    return maxLength;
  }

  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }
}
