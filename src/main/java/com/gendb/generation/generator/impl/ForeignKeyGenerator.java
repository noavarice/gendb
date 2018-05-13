package com.gendb.generation.generator.impl;

import com.gendb.generation.GenerationContext;
import com.gendb.generation.generator.TypeGenerator;
import com.gendb.model.pure.Column;
import com.gendb.model.pure.ForeignKey;
import com.gendb.model.pure.Table;
import java.util.function.Function;

public class ForeignKeyGenerator implements TypeGenerator {

  private Function<GenerationContext, Long> getMaxId;

  @Override
  public void init(final Column column) {
    final long max = column.getType().getMax().longValue();
    final Table table = column.getTable();
    final ForeignKey fk = table.getForeignKeys().stream()
      .filter(key -> key.getColumnName().equals(column.getName()))
      .findFirst()
      .get();
    if (fk.getTargetTable().equals(table.getName())) {
      getMaxId = GenerationContext::getRowId;
    } else {
      getMaxId = context -> max;
    }
  }

  @Override
  public Object yield(final GenerationContext context) {
    return context.getRandom().getNumber(1, getMaxId.apply(context));
  }
}
