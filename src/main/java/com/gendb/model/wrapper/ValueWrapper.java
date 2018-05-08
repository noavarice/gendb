package com.gendb.model.wrapper;

import java.util.Objects;

public class ValueWrapper {

  protected final Object wrapped;

  public ValueWrapper(final Object wrapped) {
    this.wrapped = wrapped;
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
