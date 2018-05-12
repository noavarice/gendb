package com.gendb.generation.generator.impl;

import com.gendb.generation.GenerationContext;
import com.gendb.generation.RandomValueProvider;
import com.gendb.generation.generator.TypeGenerator;
import com.gendb.model.pure.DataType;
import java.sql.Timestamp;

public class TimestampGenerator implements TypeGenerator {

  private RandomValueProvider provider;

  private String minColumn;

  @Override
  public void init(DataType type, RandomValueProvider provider) {
    this.provider = provider;
    minColumn = type.getMinColumn() == null ? null : type.getMinColumn().getName();
  }

  @Override
  public Object yield(final GenerationContext context) {
    final Object minColumnValue = context.getValue(minColumn);
    if (minColumnValue == null) {
      return provider.getTimestamp();
    }

    return provider.getTimestamp(((Timestamp)minColumnValue).toInstant().getEpochSecond());
  }
}
