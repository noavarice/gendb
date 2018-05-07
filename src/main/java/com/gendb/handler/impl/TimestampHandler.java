package com.gendb.handler.impl;

import com.gendb.handler.TypeHandler;
import com.gendb.model.DataType;
import com.gendb.model.wrapper.DefaultWrapper;
import com.gendb.random.RandomValueProvider;

public class TimestampHandler implements TypeHandler {

  private RandomValueProvider provider;

  @Override
  public void init(DataType type, RandomValueProvider provider) {
    this.provider = provider;
  }

  @Override
  public DefaultWrapper yield() {
    return new DefaultWrapper(provider.getTimestamp());
  }
}
