package com.gendb.model.wrapper.impl;

import com.gendb.model.wrapper.ValueWrapper;

public class TimestampWrapper extends ValueWrapper {

  public TimestampWrapper(Object wrapped) {
    super(wrapped);
  }

  @Override
  public String queryRepresentation() {
    return String.format("FROM_UNIXTIME(%1$s)", wrapped.toString());
  }
}
