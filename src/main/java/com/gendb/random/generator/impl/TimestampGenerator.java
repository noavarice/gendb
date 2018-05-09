package com.gendb.random.generator.impl;

import com.gendb.random.generator.TypeGenerator;
import com.gendb.model.DataType;
import com.gendb.random.RandomValueProvider;

public class TimestampGenerator implements TypeGenerator {

  private RandomValueProvider provider;

  @Override
  public void init(DataType type, RandomValueProvider provider) {
    this.provider = provider;
  }

  @Override
  public Object yield() {
    return provider.getTimestamp();
  }
}
