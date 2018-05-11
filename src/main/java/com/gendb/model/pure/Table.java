package com.gendb.model.pure;

import com.gendb.validation.table.UniqueColumnNames;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@UniqueColumnNames
public class Table {

  private String name;

  private String idColumnName;

  @Positive
  private int rowsCount;

  @Valid
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

  public int generatedBefore(final String col1, final String col2) {
    final Optional<ValueOrder> optionalOrder = valueOrders.stream()
      .filter(o -> o.getColumns().containsAll(Arrays.asList(col1, col2)))
      .findAny();
    if (!optionalOrder.isPresent()) {
      return 0;
    }

    final ValueOrder order = optionalOrder.get();
    return order.getColumns().indexOf(col1) < order.getColumns().indexOf(col2) ? -1 : 1;
  }

  public List<Integer> getColumnGenerationOrder() {
    final List<String> columnNames = columns.stream().map(Column::getName).collect(Collectors.toList());
    final List<String> copy = new ArrayList<>(columnNames);
    copy.sort(this::generatedBefore);
    return copy.stream().map(columnNames::indexOf).collect(Collectors.toList());
  }
}
