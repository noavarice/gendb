package com.gendb.validation.type.validator;

import com.gendb.model.validating.ValidatingDataType;
import com.gendb.util.TypeUtils;
import com.gendb.validation.type.ProperMinAndMaxLength;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Checks for string data type whether specified minimum length is more than maximum.
 */
public class ProperMinAndMaxLengthValidator implements ConstraintValidator<ProperMinAndMaxLength, ValidatingDataType> {

  @Override
  public boolean isValid(final ValidatingDataType type, final ConstraintValidatorContext context) {
    if (!TypeUtils.isString(type.getName()) || type.getLength() != null) {
      return true;
    }

    return type.getMinLength() <= type.getMaxLength();
  }
}
