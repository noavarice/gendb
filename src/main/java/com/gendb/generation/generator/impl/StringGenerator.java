package com.gendb.generation.generator.impl;

import com.gendb.generation.GenerationContext;
import com.gendb.generation.generator.TypeGenerator;
import com.gendb.model.pure.Column;

public class StringGenerator implements TypeGenerator {

  private int length;

  @Override
  public void init(final Column column) {
    length = column.getType().getLength();
  }

  @Override
  public Object yield(final GenerationContext context) {
    return context.getRandom().getAlphanumericString(length);
  }
}
