package com.gendb.handler.impl;

import com.gendb.handler.TypeHandler;
import com.gendb.model.DataType;
import com.gendb.model.wrapper.DefaultWrapper;
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
  public DefaultWrapper yield() {
    return new DefaultWrapper(provider.getDecimal(precision, scale));
  }
}
