package com.gendb.model;

import com.gendb.random.generator.TypeGenerator;
import com.gendb.model.wrapper.ValueWrapper;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

class ValueTupleIterator implements Iterator<List<ValueWrapper>> {

  private final int total;

  private int current;

  private final List<TypeGenerator> handlers;

  ValueTupleIterator(final int count, final List<TypeGenerator> handlers) {
    this.total = count;
    this.current = 0;
    this.handlers = handlers;
  }

  @Override
  public boolean hasNext() {
    return current < total;
  }

  @Override
  public List<ValueWrapper> next() {
    ++current;
    return handlers.stream().map(TypeGenerator::yield).collect(Collectors.toList());
  }
}
