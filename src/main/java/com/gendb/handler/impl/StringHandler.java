package com.gendb.handler.impl;

import com.gendb.handler.TypeHandler;
import com.gendb.model.DataType;
import com.gendb.model.wrapper.DefaultWrapper;
import com.gendb.model.wrapper.impl.StringDateWrapper;
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
  public DefaultWrapper yield() {
    return new StringDateWrapper(provider.getAlphanumericString(length));
  }
}
