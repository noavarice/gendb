package com.gendb.validation.type.validator;

import com.gendb.model.validating.ValidatingDataType;
import com.gendb.util.TypeUtils;
import com.gendb.validation.type.StringPropertiesPresent;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Checks for string data types that length attributes are properly set.
 * One of two cases are allowed: specified single 'length' attribute, so having generated strings
 * with fixed length, or 'length' attribute is missed and both 'minLength' and 'maxLength'
 * attribute values are set.
 */
public class StringPropertiesPresentValidator implements ConstraintValidator<StringPropertiesPresent, ValidatingDataType> {

  @Override
  public boolean isValid(final ValidatingDataType type, final ConstraintValidatorContext context) {
    if (!TypeUtils.isString(type.getName())) {
      return true;
    }

    final Integer fixedLength = type.getLength();
    final Integer minLength = type.getMinLength();
    final Integer maxLength = type.getMaxLength();
    if (fixedLength == null) {
      return minLength != null && maxLength != null;
    }

    return minLength == null && maxLength == null;
  }
}
