package com.gendb.model.validating;

import com.gendb.validation.distribution.CompletePercentage;
import com.gendb.validation.distribution.OrderedPoints;
import java.util.List;
import javax.validation.Valid;

@OrderedPoints
@CompletePercentage
public class ValidatingDistribution {

  private double firstPoint;

  @Valid
  private List<ValidatingDistributionPoint> points;

  public double getFirstPoint() {
    return firstPoint;
  }

  public void setFirstPoint(double firstPoint) {
    this.firstPoint = firstPoint;
  }

  public List<ValidatingDistributionPoint> getPoints() {
    return points;
  }

  public void setPoints(List<ValidatingDistributionPoint> points) {
    this.points = points;
  }
}
