package com.gendb.model;

import com.gendb.validation.table.ValidIdColumns;
import java.util.List;
import javax.validation.Valid;

@ValidIdColumns
public class Table {

  private String name;

  @Valid
  private List<Column> columns;

  private List<String> id;

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

  public List<String> getId() {
    return id;
  }

  public void setId(List<String> id) {
    this.id = id;
  }
}
