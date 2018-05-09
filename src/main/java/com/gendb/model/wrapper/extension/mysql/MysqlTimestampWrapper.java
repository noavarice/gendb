package com.gendb.model.wrapper.extension.mysql;

import com.gendb.model.wrapper.ValueWrapper;

public class MysqlTimestampWrapper extends ValueWrapper {

  @Override
  public String queryRepresentation() {
    return String.format("FROM_UNIXTIME(%1$s)", wrapped.toString());
  }
}
