package com.gendb.generation.generator;

import com.gendb.exception.GenerationException;
import com.gendb.generation.GenerationContext;
import com.gendb.model.pure.Column;

public interface TypeGenerator {

  default void init(final Column column) throws GenerationException {}

  Object yield(final GenerationContext context);
}
