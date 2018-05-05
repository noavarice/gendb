package com.gendb.model;

import com.gendb.validation.table.ValidIdColumnName;
import java.util.List;
import javax.validation.Valid;

@ValidIdColumnName
public class Table {

  private String name;

  private String idColumnName;

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
}
