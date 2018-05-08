package com.gendb.random.generator.impl;

import com.gendb.random.generator.TypeGenerator;
import com.gendb.model.DataType;
import com.gendb.model.wrapper.ValueWrapper;
import com.gendb.model.wrapper.impl.StringDateWrapper;
import com.gendb.random.RandomValueProvider;

public class StringGenerator implements TypeGenerator {

  private RandomValueProvider provider;

  private int length;

  @Override
  public void init(final DataType type, final RandomValueProvider provider) {
    this.provider = provider;
    length = type.getLength();
  }

  @Override
  public ValueWrapper yield() {
    return new StringDateWrapper(provider.getAlphanumericString(length));
  }
}
