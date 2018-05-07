package com.gendb.model.wrapper.impl;

import com.gendb.model.wrapper.DefaultWrapper;

public class TimestampWrapper extends DefaultWrapper {

  public TimestampWrapper(Object wrapped) {
    super(wrapped);
  }

  @Override
  public String processed() {
    return String.format("FROM_UNIXTIME(%1$s)", wrapped.toString());
  }
}
