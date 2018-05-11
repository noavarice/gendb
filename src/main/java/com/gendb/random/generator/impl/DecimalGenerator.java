package com.gendb.random.generator.impl;

import com.gendb.model.pure.DataType;
import com.gendb.random.RandomValueProvider;
import com.gendb.random.generator.TypeGenerator;

public class DecimalGenerator implements TypeGenerator {

  private RandomValueProvider provider;

  private int precision;

  private int scale;

  @Override
  public void init(final DataType type, final RandomValueProvider provider) {
    this.provider = provider;
    precision = type.getPrecision();
    scale = type.getScale();
  }

  @Override
  public Object yield() {
    return provider.getDecimal(precision, scale);
  }
}
