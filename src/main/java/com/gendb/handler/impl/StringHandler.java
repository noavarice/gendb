package com.gendb.handler.impl;

import com.gendb.handler.TypeHandler;
import com.gendb.model.DataType;
import com.gendb.random.RandomValueProvider;

public class StringHandler implements TypeHandler {

  private int length;

  @Override
  public void init(final DataType type) {
    length = type.getLength();
  }

  @Override
  public Object yield(final RandomValueProvider provider) {
    return provider.getString(length);
  }
}
