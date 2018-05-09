package com.gendb.random.generator.impl;

import com.gendb.random.generator.TypeGenerator;
import com.gendb.model.DataType;
import com.gendb.random.RandomValueProvider;

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
