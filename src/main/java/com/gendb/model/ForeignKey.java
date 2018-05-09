package com.gendb.model;

public class ForeignKey {

  private String targetTable;

  private String columnName;

  public String getTargetTable() {
    return targetTable;
  }

  public void setTargetTable(String targetTable) {
    this.targetTable = targetTable;
  }

  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }
}
