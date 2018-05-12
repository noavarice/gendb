package com.gendb.generation.generator.impl;

import com.gendb.generation.GenerationContext;
import com.gendb.generation.RandomValueProvider;
import com.gendb.generation.generator.TypeGenerator;
import com.gendb.model.pure.DataType;

public class TimestampGenerator implements TypeGenerator {

  private RandomValueProvider provider;

  @Override
  public void init(DataType type, RandomValueProvider provider) {
    this.provider = provider;
  }

  @Override
  public Object yield(final GenerationContext context) {
    return provider.getTimestamp();
  }
}
