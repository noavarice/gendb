package com.gendb.model;

import com.gendb.handler.TypeHandler;
import com.gendb.model.wrapper.DefaultWrapper;
import com.gendb.random.RandomValueProvider;
import com.gendb.validation.table.UniqueColumnNames;
import com.gendb.validation.table.ValidIdColumnName;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@ValidIdColumnName
@UniqueColumnNames
public class Table {

  private String name;

  private String idColumnName;

  @Positive
  private int rowsCount;

  @Valid
  private List<Column> columns;

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
    for (final Column col: columns) {
      sj.add(col.getColumnDefinition());
    }

    return String.format("CREATE TABLE %1$s %2$s;", name, sj.toString());
  }

  public String getInsertStatement() {
    final String columnNames = columns.stream()
      .map(Column::getName)
      .collect(Collectors.joining(","));
    return String.format("INSERT INTO %1$s (%2$s) VALUES\n", name, columnNames);
  }

  public Iterator<List<DefaultWrapper>> getValuesIterator(final RandomValueProvider provider) {
    final List<TypeHandler> handlers = columns.stream()
      .map(Column::getType)
      .map(type -> type.getHandler(provider))
      .collect(Collectors.toList());
    return new ValueTupleIterator(rowsCount, handlers);
  }
}
