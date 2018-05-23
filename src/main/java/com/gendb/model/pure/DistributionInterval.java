package com.gendb.model.pure;

public class DistributionInterval {

  private final double min, max;

  private final double percentage;

  public DistributionInterval(final double min, final double max, final double percentage) {
    this.min = min;
    this.max = max;
    this.percentage = percentage;
  }

  public double getPercentage() {
    return percentage;
  }

  public double getMin() {
    return min;
  }

  public double getMax() {
    return max;
  }
}
