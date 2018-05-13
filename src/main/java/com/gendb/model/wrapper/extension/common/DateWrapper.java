package com.gendb.model.wrapper.extension.common;

import com.gendb.model.wrapper.ValueWrapper;

public class DateWrapper extends ValueWrapper {

  @Override
  public String queryRepresentation() {
    return wrapped == null ? "null" : String.format("\'%1$s\'", wrapped.toString().split(" ")[0]);
  }
}
