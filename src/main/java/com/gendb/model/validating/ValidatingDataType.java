package com.gendb.model.validating;

import com.gendb.validation.Violations;
import com.gendb.validation.stage.FirstStage;
import com.gendb.validation.type.DecimalPropertiesPresent;
import com.gendb.validation.type.DictionaryExists;
import com.gendb.validation.type.HandlerClassExist;
import com.gendb.validation.type.PrecisionLessThanScale;
import com.gendb.validation.type.ValidNumericBoundaries;
import javax.validation.GroupSequence;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@DecimalPropertiesPresent
@ValidNumericBoundaries
@DictionaryExists
@PrecisionLessThanScale(groups = FirstStage.class)
@GroupSequence({ValidatingDataType.class, FirstStage.class})
public class ValidatingDataType {

  private String name;

  private boolean nullable;

  @HandlerClassExist
  private String handlerClass;

  @Positive(message = Violations.NON_POSITIVE_PRECISION)
  private Integer precision;

  @PositiveOrZero(message = Violations.NEGATIVE_PRECISION)
  private Integer scale;

  @Positive
  private Integer length;

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
}
