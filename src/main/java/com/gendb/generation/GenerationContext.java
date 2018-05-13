package com.gendb.generation;

import com.gendb.model.pure.Column;
import com.gendb.model.pure.Table;
import com.gendb.model.wrapper.ValueWrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GenerationContext {

  private final Map<String, ValueWrapper> columnToWrapper;

  private final String idColumnName;

  private final RandomValueProvider random;

  private long rowId = 0;

  GenerationContext(final Table table, final List<ValueWrapper> row, final RandomValueProvider r) {
    random = r;
    final Map<String, Integer> columnToIndex = table.getColumns().stream()
      .collect(Collectors.toMap(Column::getName, table.getColumns()::indexOf));
    columnToWrapper = new HashMap<>();
    for (final String columnName: columnToIndex.keySet()) {
      columnToWrapper.put(columnName, row.get(columnToIndex.get(columnName)));
    }

    idColumnName = table.getIdColumnName();
  }

  public Object getValue(final String columnName) {
    if (columnName == null) {
      return null;
    } else if (columnName.equals(idColumnName)) {
      return rowId;
    }

    return columnToWrapper.get(columnName).plain();
  }

  public long getRowId() {
    return rowId;
  }

  void setRowId(long rowId) {
    this.rowId = rowId;
  }

  public RandomValueProvider getRandom() {
    return random;
  }
}
