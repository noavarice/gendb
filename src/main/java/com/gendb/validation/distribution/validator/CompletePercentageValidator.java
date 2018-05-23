package com.gendb.validation.distribution.validator;

import com.gendb.model.validating.ValidatingDistribution;
import com.gendb.model.validating.ValidatingDistributionPoint;
import com.gendb.validation.distribution.CompletePercentage;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CompletePercentageValidator implements ConstraintValidator<CompletePercentage, ValidatingDistribution> {

  @Override
  public boolean isValid(final ValidatingDistribution distribution, final ConstraintValidatorContext context) {
    if (distribution == null) {
      return true;
    }

    return distribution.getPoints().stream()
      .map(ValidatingDistributionPoint::getPercentage)
      .reduce(0d, Double::sum) == 100.0;
  }
}
