package com.gendb.validation.type.validator;

import com.gendb.model.validating.ValidatingDataType;
import com.gendb.validation.type.DecimalPropertiesPresent;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DecimalPropertiesPresentValidator implements ConstraintValidator<DecimalPropertiesPresent, ValidatingDataType> {

  @Override
  public boolean isValid(final ValidatingDataType type, ConstraintValidatorContext context) {
    if (!type.getName().equals("decimal")) {
      return true;
    }

    return type.getPrecision() != null && type.getScale() != null;
  }
}
