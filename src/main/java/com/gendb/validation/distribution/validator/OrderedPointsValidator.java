package com.gendb.validation.distribution.validator;

import com.gendb.model.validating.ValidatingDistribution;
import com.gendb.model.validating.ValidatingDistributionPoint;
import com.gendb.util.Fn;
import com.gendb.validation.distribution.OrderedPoints;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OrderedPointsValidator implements ConstraintValidator<OrderedPoints, ValidatingDistribution> {

  @Override
  public boolean isValid(final ValidatingDistribution distribution, final ConstraintValidatorContext context) {
    if (distribution == null) {
      return true;
    }

    final double firstPoint = distribution.getFirstPoint();
    final List<Double> points = new LinkedList<>(Collections.singletonList(firstPoint));
    points.addAll(Fn.map(distribution.getPoints(), ValidatingDistributionPoint::getPoint));
    for (int i = 1; i < points.size(); ++i) {
      if (points.get(i - 1).compareTo(points.get(i)) >= 0) {
        return false;
      }
    }

    return true;
  }
}
