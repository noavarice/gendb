package com.gendb.model.wrapper.extension.postgres;

import com.gendb.model.wrapper.ValueWrapper;

public class PostgresTimestampWrapper extends ValueWrapper {

  @Override
  public String queryRepresentation() {
    return String.format("TO_TIMESTAMP(%1$s)", wrapped.toString());
  }
}
