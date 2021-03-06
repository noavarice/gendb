package com.gendb.model.validating;

import com.gendb.validation.stage.FirstStage;
import com.gendb.validation.stage.SecondStage;
import com.gendb.validation.stage.ThirdStage;
import com.gendb.validation.table.DuplicateColumnsInValueOrder;
import com.gendb.validation.table.KnownColumnsInValueOrder;
import com.gendb.validation.table.OrderedColumnsHaveCommonType;
import com.gendb.validation.table.StrictColumnOrder;
import com.gendb.validation.table.UniqueColumnNames;
import java.util.List;
import javax.validation.GroupSequence;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@UniqueColumnNames
@KnownColumnsInValueOrder(groups = FirstStage.class)
@DuplicateColumnsInValueOrder(groups = FirstStage.class)
@OrderedColumnsHaveCommonType(groups = SecondStage.class)
@StrictColumnOrder(groups = ThirdStage.class)
@GroupSequence({ValidatingTable.class, FirstStage.class, SecondStage.class, ThirdStage.class})
public class ValidatingTable {

  private String name;

  private String idColumnName;

  @Positive
  private int rowsCount;

  @Valid
  private List<ValidatingColumn> columns;

  private List<ValidatingForeignKey> foreignKeys;

  private List<ValidatingValueOrder> valueOrders;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<ValidatingColumn> getColumns() {
    return columns;
  }

  public void setColumns(List<ValidatingColumn> columns) {
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

  public List<ValidatingForeignKey> getForeignKeys() {
    return foreignKeys;
  }

  public void setForeignKeys(List<ValidatingForeignKey> foreignKeys) {
    this.foreignKeys = foreignKeys;
  }

  public List<ValidatingValueOrder> getValueOrders() {
    return valueOrders;
  }

  public void setValueOrders(List<ValidatingValueOrder> valueOrders) {
    this.valueOrders = valueOrders;
  }
}
