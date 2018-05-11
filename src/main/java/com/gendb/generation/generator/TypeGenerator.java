package com.gendb.generation.generator;

import com.gendb.generation.GenerationContext;
import com.gendb.generation.RandomValueProvider;
import com.gendb.model.pure.DataType;

public interface TypeGenerator {

  default void init(final DataType type, final RandomValueProvider provider) {}

  Object yield(final GenerationContext context);
}
