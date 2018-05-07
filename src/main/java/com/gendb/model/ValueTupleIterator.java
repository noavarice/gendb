package com.gendb.model;

import com.gendb.handler.TypeHandler;
import com.gendb.model.wrapper.DefaultWrapper;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

class ValueTupleIterator implements Iterator<List<DefaultWrapper>> {

  private final int total;

  private int current;

  private final List<TypeHandler> handlers;

  ValueTupleIterator(final int count, final List<TypeHandler> handlers) {
    this.total = count;
    this.current = 0;
    this.handlers = handlers;
  }

  @Override
  public boolean hasNext() {
    return current < total;
  }

  @Override
  public List<DefaultWrapper> next() {
    ++current;
    return handlers.stream().map(TypeHandler::yield).collect(Collectors.toList());
  }
}
