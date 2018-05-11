package com.gendb.generation;

import com.gendb.model.wrapper.ValueWrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerationContext {

  private final Map<String, ValueWrapper> columnToWrapper;

  GenerationContext(final List<ValueWrapper> row, final Map<String, Integer> indexMap) {
    columnToWrapper = new HashMap<>();
    for (final String columnName: indexMap.keySet()) {
      columnToWrapper.put(columnName, row.get(indexMap.get(columnName)));
    }
  }

  public Object getValue(final String columnName) {
    if (columnName == null) {
      return null;
    }

    return columnToWrapper.get(columnName).plain();
  }
}
