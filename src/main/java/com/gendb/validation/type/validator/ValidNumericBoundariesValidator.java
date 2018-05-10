package com.gendb.validation.type.validator;

import com.gendb.TypeUtils;
import com.gendb.model.DataType;
import com.gendb.validation.ValidationUtils;
import com.gendb.validation.type.ValidNumericBoundaries;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidNumericBoundariesValidator implements ConstraintValidator<ValidNumericBoundaries, DataType> {

  @Override
  public boolean isValid(final DataType type, final ConstraintValidatorContext context) {
    if (!TypeUtils.isNumeric(type.getName())) {
      return true;
    }

    if (type.getMax() == null || type.getMax() == null) {
      return true;
    }

  if (type.getMin() > type.getMax()) {
      ValidationUtils.addViolation(context, type.getMin(), type.getMax());
      return false;
    }

    return true;
  }
}
