package com.gendb.model.validating;

import javax.validation.Valid;

public class ValidatingColumn {

  @Valid
  private ValidatingDataType type;

  private String name;

  public ValidatingDataType getType() {
    return type;
  }

  public void setType(ValidatingDataType type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
