package com.gendb.random.generator;

import com.gendb.model.pure.DataType;
import com.gendb.random.RandomValueProvider;

public interface TypeGenerator {

  default void init(final DataType type, final RandomValueProvider provider) {}

  Object yield();
}
