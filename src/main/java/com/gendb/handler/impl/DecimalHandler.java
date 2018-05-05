package com.gendb.handler.impl;

import com.gendb.handler.TypeHandler;
import com.gendb.model.DataType;
import com.gendb.random.RandomValueProvider;

public class DecimalHandler implements TypeHandler {

  private int precision;

  private int scale;

  @Override
  public void init(DataType type) {
    precision = type.getPrecision();
    scale = type.getScale();
  }

  @Override
  public Object yield(RandomValueProvider provider) {
    return provider.getDecimal(precision, scale);
  }
}
