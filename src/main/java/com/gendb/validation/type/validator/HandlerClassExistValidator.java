package com.gendb.validation.type.validator;

import com.gendb.validation.ValidationUtils;
import com.gendb.validation.type.HandlerClassExist;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HandlerClassExistValidator implements ConstraintValidator<HandlerClassExist, String> {

  @Override
  public boolean isValid(String handler, ConstraintValidatorContext context) {
    if (handler == null || handler.isEmpty()) {
      return true;
    }

    try {
      Class.forName(handler, false, this.getClass().getClassLoader());
    } catch (ClassNotFoundException e) {
      ValidationUtils.addViolation(context, handler);
      return false;
    }

    return true;
  }
}
