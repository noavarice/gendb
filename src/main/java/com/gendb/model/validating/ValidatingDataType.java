package com.gendb.model.validating;

import com.gendb.validation.stage.FirstStage;
import com.gendb.validation.type.DecimalPropertiesPresent;
import com.gendb.validation.type.DictionaryExists;
import com.gendb.validation.type.HandlerClassExist;
import com.gendb.validation.type.PrecisionLessThanScale;
import com.gendb.validation.type.ProperMinAndMaxLength;
import com.gendb.validation.type.StringPropertiesPresent;
import com.gendb.validation.type.ValidNumericBoundaries;
import javax.validation.GroupSequence;

@DecimalPropertiesPresent
@ValidNumericBoundaries
@DictionaryExists
@StringPropertiesPresent
@PrecisionLessThanScale(groups = FirstStage.class)
@ProperMinAndMaxLength(groups = FirstStage.class)
@GroupSequence({ValidatingDataType.class, FirstStage.class})
public class ValidatingDataType {

  private String name;

  private boolean nullable;

  @HandlerClassExist
  private String handlerClass;

  private Integer precision;

  private Integer scale;

  private Integer length;

  private Integer minLength;

  private Integer maxLength;

  private Double min;

  private Double max;

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
