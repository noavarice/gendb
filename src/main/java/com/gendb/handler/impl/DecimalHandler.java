package com.gendb.handler.impl;

import com.gendb.handler.TypeHandler;
import com.gendb.model.DataType;
import com.gendb.random.RandomValueProvider;

public class DecimalHandler implements TypeHandler {

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
