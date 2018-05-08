package com.gendb.model;

import com.gendb.random.generator.TypeGenerator;
import com.gendb.random.RandomValueProvider;
import com.gendb.validation.Violations;
import com.gendb.validation.type.DecimalPropertiesPresent;
import com.gendb.validation.type.HandlerClassExist;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@DecimalPropertiesPresent
public class DataType {

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

  private boolean unsigned;

  private Double min;

  private Double max;

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

  public String getTypeDefinition() {
    final StringBuilder sb = new StringBuilder(name.toUpperCase());
    if (name.equals("decimal")) {
      sb.append(String.format("(%1$s,%2$s)", precision, scale));
    } else if (name.equals("char") || name.equals("varchar")) {
      sb.append(String.format("(%1$s)", length));
    }

    if (!nullable) {
      sb.append(" NOT NULL");
    }

    return sb.toString();
  }

  public TypeGenerator getHandler(final RandomValueProvider provider) {
    try {
      final TypeGenerator h = (((Class<? extends TypeGenerator>) Class.forName(handlerClass)).newInstance());
      h.init(this, provider);
      return h;
    } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
      return null;
    }
  }

  public boolean isUnsigned() {
    return unsigned;
  }

  public void setUnsigned(boolean unsigned) {
    this.unsigned = unsigned;
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
}
