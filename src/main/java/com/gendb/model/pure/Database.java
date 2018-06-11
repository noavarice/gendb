package com.gendb.model.pure;

import java.util.List;

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

  private List<Table> tables;

  private SupportedDbms dbmsName;

  private int batchSize;

  private boolean tablesSorted = false;

  private int fkCounter = 1;

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

  SupportedDbms getDbmsName() {
    return dbmsName;
  }

  public void setDbmsName(SupportedDbms dbmsName) {
    this.dbmsName = dbmsName;
  }

  public int getBatchSize() {
    return batchSize;
  }

  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }
}
