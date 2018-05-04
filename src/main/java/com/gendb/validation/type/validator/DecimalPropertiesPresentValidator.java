package com.gendb.validation.type.validator;

import com.gendb.model.DataType;
import com.gendb.validation.type.DecimalPropertiesPresent;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DecimalPropertiesPresentValidator implements ConstraintValidator<DecimalPropertiesPresent, DataType> {

  @Override
  public boolean isValid(final DataType type, ConstraintValidatorContext context) {
    if (!type.getName().equals("decimal")) {
      return true;
    }

    return type.getPrecision() != null && type.getScale() != null;
  }
}
