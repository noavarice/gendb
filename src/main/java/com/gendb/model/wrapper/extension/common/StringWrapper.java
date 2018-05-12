package com.gendb.model.wrapper.extension.common;

import com.gendb.model.wrapper.ValueWrapper;
import java.util.Date;

/**
 * Wraps {@link String} or {@link Date} values
 * to return queryRepresentation value enclosed by quotes
 */
public class StringWrapper extends ValueWrapper {

  @Override
  public String queryRepresentation() {
    return wrapped == null ? "null" : String.format("\'%1$s\'", wrapped);
  }
}
