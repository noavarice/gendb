package com.gendb.model.pure;

public class ConcreteDistributionInterval {

  private long min, max;

  private long count;

  public ConcreteDistributionInterval() {
  }

  public ConcreteDistributionInterval(final long min, final long max, final int count) {
    this.min = min;
    this.max = max;
    this.count = count;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  public long getMin() {
    return min;
  }

  public void setMin(long min) {
    this.min = min;
  }

  public long getMax() {
    return max;
  }

  public void setMax(long max) {
    this.max = max;
  }
}
