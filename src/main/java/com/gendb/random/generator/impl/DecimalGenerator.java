package com.gendb.random.generator.impl;

import com.gendb.random.generator.TypeGenerator;
import com.gendb.model.DataType;
import com.gendb.model.wrapper.ValueWrapper;
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
  public ValueWrapper yield() {
    return new ValueWrapper(provider.getDecimal(precision, scale));
  }
}
