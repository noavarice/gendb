package com.gendb.model.wrapper;

import java.util.Objects;

public class ValueWrapper {

  protected Object wrapped;

  public final void setObject(final Object obj) {
    wrapped = obj;
  }

  /**
   * Returns plain wrapped object
   */
  public final Object plain() {
    return wrapped;
  }

  /**
   * Returns string representation of object
   * suitable for script generation
   */
  public String queryRepresentation() {
    return Objects.toString(wrapped);
  }
}
