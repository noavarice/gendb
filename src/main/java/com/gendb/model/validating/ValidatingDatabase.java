package com.gendb.model.validating;

import com.gendb.validation.database.NoCyclicReferences;
import com.gendb.validation.database.UniqueTableNames;
import com.gendb.validation.database.ValidForeignKeys;
import com.gendb.validation.stage.FirstStage;
import com.gendb.validation.stage.SecondStage;
import java.util.List;
import javax.validation.GroupSequence;
import javax.validation.Valid;

@GroupSequence({ValidatingDatabase.class, FirstStage.class, SecondStage.class})
public class ValidatingDatabase {

  private String name;

  @Valid
  @UniqueTableNames(groups = FirstStage.class)
  @ValidForeignKeys(groups = FirstStage.class)
  @NoCyclicReferences(groups = SecondStage.class)
  private List<ValidatingTable> tables;

  private String dbmsName;

  private int batchSize;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<ValidatingTable> getTables() {
    return tables;
  }

  public void setTables(List<ValidatingTable> tables) {
    this.tables = tables;
  }

  public String getDbmsName() {
    return dbmsName;
  }

  public void setDbmsName(String dbmsName) {
    this.dbmsName = dbmsName;
  }

  public int getBatchSize() {
    return batchSize;
  }

  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }
}
