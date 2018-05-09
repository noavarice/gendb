package com.gendb.random.generator.impl;

import com.gendb.model.DataType;
import com.gendb.random.RandomValueProvider;
import com.gendb.random.generator.TypeGenerator;

public class StringGenerator implements TypeGenerator {

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
