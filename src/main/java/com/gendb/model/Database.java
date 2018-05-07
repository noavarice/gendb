package com.gendb.model;

import java.util.List;
import javax.validation.Valid;

public class Database {

  private String name;

  @Valid
  private List<Table> tables;

  private String dbmsName;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Table> getTables() {
    return tables;
  }

  public void setTables(List<Table> tables) {
    this.tables = tables;
  }

  public String getDbmsName() {
    return dbmsName;
  }

  public void setDbmsName(String dbmsName) {
    this.dbmsName = dbmsName;
  }

  @Override
  public String toString() {
    return name;
  }

  public String getCreateStatement() {
    return String.format("CREATE DATABASE %1$s;\n%2$s %1$s;\n",
      name,
      dbmsName.equals("mysql") ? "USE" : "\\c");
  }
}
