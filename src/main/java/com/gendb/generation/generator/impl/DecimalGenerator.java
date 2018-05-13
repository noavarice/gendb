package com.gendb.generation.generator.impl;

import com.gendb.generation.GenerationContext;
import com.gendb.generation.generator.TypeGenerator;
import com.gendb.model.pure.Column;
import com.gendb.model.pure.DataType;

public class DecimalGenerator implements TypeGenerator {

  private int precision;

  private int scale;

  @Override
  public void init(final Column column) {
    final DataType type = column.getType();
    precision = type.getPrecision();
    scale = type.getScale();
  }

  @Override
  public Object yield(final GenerationContext context) {
    return context.getRandom().getDecimal(precision, scale);
  }
}
