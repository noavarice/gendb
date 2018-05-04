package com.gendb.validation;

import javax.validation.ConstraintValidatorContext;

public class ValidationUtils {

  public static void addViolation(final ConstraintValidatorContext context, Object... args) {
    final String template = context.getDefaultConstraintMessageTemplate();
    context.buildConstraintViolationWithTemplate(String.format(template, args))
      .addConstraintViolation()
      .disableDefaultConstraintViolation();
  }
}
