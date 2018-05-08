package com.gendb.random.generator;

import com.gendb.model.DataType;
import com.gendb.model.wrapper.ValueWrapper;
import com.gendb.random.RandomValueProvider;

public interface TypeGenerator {

  default void init(final DataType type, final RandomValueProvider provider) {}

  ValueWrapper yield();
}
