package com.gendb.model.pure;

import com.gendb.validation.Violations;
import com.gendb.validation.type.DecimalPropertiesPresent;
import com.gendb.validation.type.HandlerClassExist;
import com.gendb.validation.type.ValidNumericBoundaries;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@DecimalPropertiesPresent
@ValidNumericBoundaries
public class DataType {

  private static final Set<String> INTEGER_TYPES = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER) {{
    addAll(Arrays.asList("smallint", "int", "bigint"));
  }};

  private static final Set<String> STRING_TYPES = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER) {{
    addAll(Arrays.asList("char", "varchar"));
  }};

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

  String getTypeDefinition() {
    final StringBuilder sb = new StringBuilder(name.toUpperCase());
    if (name.equals("decimal")) {
      sb.append(String.format("(%1$s,%2$s)", precision, scale));
    } else if (STRING_TYPES.contains(name)) {
      sb.append(String.format("(%1$s)", length));
    } else if (INTEGER_TYPES.contains(name) && unsigned) {
      sb.append(" UNSIGNED");
    }

    if (!nullable) {
      sb.append(" NOT NULL");
    }

    return sb.toString();
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
