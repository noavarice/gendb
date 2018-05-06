package com.gendb.handler.impl;

import com.gendb.handler.TypeHandler;
import com.gendb.model.DataType;
import com.gendb.random.RandomValueProvider;

public class StringHandler implements TypeHandler {

  private RandomValueProvider provider;

  private int length;

  @Override
  public void init(final DataType type, final RandomValueProvider provider) {
    this.provider = provider;
    length = type.getLength();
  }

  @Override
  public Object yield() {
    return provider.getAlphanumericString(length);
  }
}
