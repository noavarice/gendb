package com.gendb.model.pure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Table {

  private String name;

  private String idColumnName;

  private int rowsCount;

  private List<Column> columns;

  private List<ForeignKey> foreignKeys;

  private List<ValueOrder> valueOrders;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Column> getColumns() {
    return columns;
  }

  public void setColumns(List<Column> columns) {
    this.columns = columns;
  }

  public String getIdColumnName() {
    return idColumnName;
  }

  public void setIdColumnName(String idColumnName) {
    this.idColumnName = idColumnName;
  }

  public int getRowsCount() {
    return rowsCount;
  }

  public void setRowsCount(int rowsCount) {
    this.rowsCount = rowsCount;
  }

  public String getCreateStatement() {
    final StringJoiner sj = new StringJoiner(",", "(", ")");
    sj.add("%1$s"); // for DBMS-dependent primary key column declaration
    for (final Column col: columns) {
      sj.add(col.getColumnDefinition());
    }

    return String.format("CREATE TABLE %1$s %2$s;%3$s", name, sj.toString(), System.lineSeparator());
  }

  public String getInsertStatement() {
    final String columnNames = columns.stream()
      .map(Column::getName)
      .collect(Collectors.joining(","));
    return String.format("INSERT INTO %1$s (%2$s) VALUES", name, columnNames);
  }

  public List<DataType> getColumnTypes() {
    return columns.stream().map(Column::getType).collect(Collectors.toList());
  }

  public List<ForeignKey> getForeignKeys() {
    return foreignKeys;
  }

  public void setForeignKeys(List<ForeignKey> foreignKeys) {
    this.foreignKeys = foreignKeys;
  }

  public List<ValueOrder> getValueOrders() {
    return valueOrders;
  }

  public void setValueOrders(List<ValueOrder> valueOrders) {
    this.valueOrders = valueOrders;
  }

  private boolean generatedBefore(final String col1, final String col2) {
    final Optional<ValueOrder> optionalOrder = valueOrders.stream()
      .filter(o -> o.getColumns().containsAll(Arrays.asList(col1, col2)))
      .findAny();
    if (!optionalOrder.isPresent()) {
      return true;
    }

    final ValueOrder order = optionalOrder.get();
    return order.getColumns().indexOf(col1) < order.getColumns().indexOf(col2);
  }

  public List<Integer> getColumnGenerationOrder() {
    final List<String> originalOrder = columns.stream().map(Column::getName).collect(Collectors.toList());
    final List<String> generationOrder = new ArrayList<>(originalOrder.size());
    for (final ValueOrder order: valueOrders) {
      for (final String newColumn: order.getColumns()) {
        generationOrder.remove(newColumn);
        int i = generationOrder.size() - 1;
        while (i >= 0 && generatedBefore(newColumn, generationOrder.get(i))) {
          --i;
        }

        generationOrder.add(i + 1, newColumn);
      }
    }

    for (final String column: originalOrder) {
      if (!generationOrder.contains(column)) {
        generationOrder.add(column);
      }
    }

    return generationOrder.stream().map(originalOrder::indexOf).collect(Collectors.toList());
  }
}
