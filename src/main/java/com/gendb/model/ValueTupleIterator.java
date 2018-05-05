package com.gendb.model;

import com.gendb.handler.TypeHandler;
import com.gendb.random.RandomValueProvider;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

class ValueTupleIterator implements Iterator<List<Object>> {

  private final int total;

  private int current;

  private final List<TypeHandler> handlers;

  private final RandomValueProvider provider;

  ValueTupleIterator(final int count, final List<TypeHandler> handlers, final RandomValueProvider provider) {
    this.total = count;
    this.current = 0;
    this.handlers = handlers;
    this.provider = provider;
  }

  @Override
  public boolean hasNext() {
    return current < total;
  }

  @Override
  public List<Object> next() {
    ++current;
    return handlers.stream().map(h -> h.yield(provider)).collect(Collectors.toList());
  }
}
