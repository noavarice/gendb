package com.gendb.generation.generator.impl;

import com.gendb.generation.GenerationContext;
import com.gendb.generation.RandomValueProvider;
import com.gendb.generation.generator.TypeGenerator;
import com.gendb.model.pure.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntegerGenerator implements TypeGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(IntegerGenerator.class);

  private static final long MAX_UNSIGNED_INT = (long)(Integer.MAX_VALUE) - Integer.MIN_VALUE;

  private String minColumn;

  private RandomValueProvider provider;

  private long min, max;

  @Override
  public void init(final DataType type, final RandomValueProvider provider) {
    this.provider = provider;
    minColumn = type.getMinColumn() == null ? null : type.getMinColumn().getName();
    if (type.isUnsigned()) {
      min = (long)(type.getMin() == null ? 0 : type.getMin());
      max = (long)(type.getMax() == null ? MAX_UNSIGNED_INT : type.getMax());
    } else {
      min = (long)(type.getMin() == null ? Integer.MIN_VALUE : type.getMin());
      max = (long)(type.getMax() == null ? Integer.MAX_VALUE : type.getMax());
    }
  }

  @Override
  public Object yield(final GenerationContext context) {
    final Object minColumnValue = context.getValue(minColumn);
    if (minColumnValue == null) {
      return provider.getNumber(min, max);
    }

    if (minColumnValue instanceof Long) {
      return provider.getNumber((Long)minColumnValue, max);
    }

    LOGGER.error("Column which value must be a minimum has unexpected type '{}'", minColumnValue.getClass());
    return null;
  }
}
