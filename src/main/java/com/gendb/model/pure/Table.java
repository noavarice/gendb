package com.gendb.model.pure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Table {

  private static int fkCounter = 0;

  private static final String FK_DECLARATION_TEMPLATE =
    "ADD CONSTRAINT fk%1$d FOREIGN KEY (%2$s) REFERENCES %3$s(%4$s)";

  private static final String PK_DECLARATION_TEMPLATE = "%1$s %2$s PRIMARY KEY";

  private static final Map<String, String> DBMS_TO_PK_TYPE = new HashMap<String, String>() {{
    put("mysql", "INTEGER AUTO_INCREMENT");
    put("postgres", "SERIAL");
  }};

  private String name;

  private String idColumnName;

  private int rowsCount;

  private List<Column> columns;

  private List<ForeignKey> foreignKeys;

  private List<ValueOrder> valueOrders;

  public static Database database;

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

  private String getPrimaryKeyDeclaration(final String columnName) {
    final String dbmsName = database.getDbmsName();
    return String.format(PK_DECLARATION_TEMPLATE, columnName, DBMS_TO_PK_TYPE.get(dbmsName));
  }

  public String getCreateStatement() {
    final StringJoiner sj = new StringJoiner(",", "(", ")");
    sj.add(getPrimaryKeyDeclaration(idColumnName));
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

  public String getForeignKeyDeclarations() {
    if (foreignKeys.isEmpty()) {
      return "";
    }

    final Map<String, String> nameToIdColumn = database.getTables().stream()
      .collect(Collectors.toMap(Table::getName, t -> t.idColumnName));
    final StringJoiner sj = new StringJoiner(",");
    for (final ForeignKey fk: foreignKeys) {
      final String idColName = nameToIdColumn.get(fk.getTargetTable());
      sj.add(String.format(
        FK_DECLARATION_TEMPLATE,
        fkCounter++,
        fk.getColumnName(),
        fk.getTargetTable(),
        idColName));
    }

    return String.format("ALTER TABLE %1$s %2$s;%3$s", name, sj.toString(), System.lineSeparator());
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
