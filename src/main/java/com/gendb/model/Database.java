package com.gendb.model;

import com.gendb.validation.stage.FirstStage;
import com.gendb.validation.database.NoCyclicReferences;
import com.gendb.validation.database.UniqueTableNames;
import com.gendb.validation.database.ValidForeignKeys;
import com.gendb.validation.stage.SecondStage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.GroupSequence;
import javax.validation.Valid;

@GroupSequence({Database.class, FirstStage.class, SecondStage.class})
public class Database {

  private static boolean depend(final Table t1, final Table t2) {
    return t1.getForeignKeys().stream()
      .map(ForeignKey::getTargetTable)
      .anyMatch(t2.getName()::equals);
  }

  private static int compareTables(final Table t1, final Table t2) {
    if (depend(t1, t2)) {
      return 1;
    }

    if (depend(t2, t1)) {
      return -1;
    }

    return 0;
  }

  private static final Map<String, String> DBMS_TO_PK_TYPE = new HashMap<String, String>() {{
    put("mysql", "INTEGER AUTO_INCREMENT");
    put("postgres", "SERIAL");
  }};

  private static final String PK_DECLARATION_TEMPLATE = "%1$s %2$s PRIMARY KEY";

  private String name;

  @Valid
  @UniqueTableNames(groups = FirstStage.class)
  @ValidForeignKeys(groups = FirstStage.class)
  @NoCyclicReferences(groups = SecondStage.class)
  private List<Table> tables;

  private String dbmsName;

  private boolean tablesSorted = false;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Table> getTables() {
    if (tablesSorted) {
      return tables;
    }

    tablesSorted = true;
    tables.sort(Database::compareTables);
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
