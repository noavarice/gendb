package com.gendb.model.wrapper.impl;

import com.gendb.model.wrapper.DefaultWrapper;
import java.util.Date;

/**
 * Wraps {@link String} or {@link Date} values
 * to return processed value enclosed by quotes
 */
public class StringDateWrapper extends DefaultWrapper {

  public StringDateWrapper(Object wrapped) {
    super(wrapped);
  }

  @Override
  public String processed() {
    return wrapped == null ? "null" : String.format("\'%1$s\'", wrapped);
  }
}
