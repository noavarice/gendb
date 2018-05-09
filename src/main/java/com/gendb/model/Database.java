package com.gendb.model;

import com.gendb.validation.database.UniqueTableNames;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

public class Database {

  private static final Map<String, String> DBMS_TO_PK_TYPE = new HashMap<String, String>() {{
    put("mysql", "INTEGER AUTO_INCREMENT");
    put("postgres", "SERIAL");
  }};

  private static final String PK_DECLARATION_TEMPLATE = "%1$s %2$s PRIMARY KEY";

  private String name;

  @Valid
  @UniqueTableNames
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

  public String getPrimaryKeyDeclaration(final String columnName) {
    return String.format(PK_DECLARATION_TEMPLATE, columnName, DBMS_TO_PK_TYPE.get(dbmsName));
  }
}
