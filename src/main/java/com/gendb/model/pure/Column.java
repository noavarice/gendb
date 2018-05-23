package com.gendb.model.pure;

import java.util.List;

public class Column {

  private DataType type;

  private String name;

  private Table table;

  private List<DistributionInterval> distributionIntervals;

  public DataType getType() {
    return type;
  }

  public void setType(DataType type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  String getColumnDefinition() {
    return name + ' ' + type.getTypeDefinition();
  }

  public Table getTable() {
    return table;
  }

  public void setTable(Table table) {
    this.table = table;
  }

  public List<DistributionInterval> getDistributionIntervals() {
    return distributionIntervals;
  }

  public void setDistributionIntervals(List<DistributionInterval> points) {
    this.distributionIntervals = points;
  }
}
