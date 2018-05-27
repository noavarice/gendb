package com.gendb.validation.type.validator;

import com.gendb.model.validating.ValidatingDataType;
import com.gendb.util.Types;
import com.gendb.validation.ValidationUtils;
import com.gendb.validation.type.PrecisionLessThanScale;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PrecisionLessThanScaleValidator implements ConstraintValidator<PrecisionLessThanScale, ValidatingDataType> {

  @Override
  public boolean isValid(final ValidatingDataType type, final ConstraintValidatorContext context) {
    if (!type.getName().equals(Types.DECIMAL.getName())) {
      return true;
    }

    if (type.getScale() <= type.getPrecision()) {
      return true;
    }

    ValidationUtils.addViolation(context, type.getPrecision(), type.getScale());
    return false;
  }
}
