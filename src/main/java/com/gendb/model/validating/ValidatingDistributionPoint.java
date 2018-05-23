package com.gendb.model.validating;

import com.gendb.validation.Violations;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class ValidatingDistributionPoint {

  private double point;

  @Min(value = 0, message = Violations.DISTRIBUTION_INTERVAL_PERCENTAGE_OUT_OF_BOUNDS)
  @Max(value = 100, message = Violations.DISTRIBUTION_INTERVAL_PERCENTAGE_OUT_OF_BOUNDS)
  private double percentage;

  public double getPercentage() {
    return percentage;
  }

  public void setPercentage(double percentage) {
    this.percentage = percentage;
  }

  public double getPoint() {
    return point;
  }

  public void setPoint(double point) {
    this.point = point;
  }
}
