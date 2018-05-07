package com.gendb.model.wrapper;

import java.util.Objects;

public class DefaultWrapper {

  protected final Object wrapped;

  public DefaultWrapper(final Object wrapped) {
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
  public String processed() {
    return Objects.toString(wrapped);
  }
}
