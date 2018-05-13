package com.gendb.generation.generator.impl;

import com.gendb.generation.GenerationContext;
import com.gendb.generation.generator.TypeGenerator;
import com.gendb.model.pure.Column;
import java.sql.Timestamp;

public class TimestampGenerator implements TypeGenerator {

  private String minColumnName;

  @Override
  public void init(final Column column) {
    final Column minColumn = column.getType().getMinColumn();
    minColumnName = minColumn == null ? null : minColumn.getName();
  }

  @Override
  public Object yield(final GenerationContext context) {
    final Object minColumnValue = context.getValue(minColumnName);
    if (minColumnValue == null) {
      return context.getRandom().getTimestamp();
    }

    return context.getRandom().getTimestamp(((Timestamp)minColumnValue).toInstant().toEpochMilli());
  }
}
