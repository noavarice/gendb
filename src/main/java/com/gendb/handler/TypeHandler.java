package com.gendb.handler;

import com.gendb.model.DataType;
import com.gendb.random.RandomValueProvider;

public interface TypeHandler {

  default void init(final DataType type) {}

  Object yield(final RandomValueProvider provider);
}
