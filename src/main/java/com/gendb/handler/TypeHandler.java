package com.gendb.handler;

import com.gendb.model.DataType;
import com.gendb.model.wrapper.DefaultWrapper;
import com.gendb.random.RandomValueProvider;

public interface TypeHandler {

  default void init(final DataType type, final RandomValueProvider provider) {}

  DefaultWrapper yield();
}
