package com.gendb.validation.type;

import com.gendb.validation.Violations;
import com.gendb.validation.type.validator.ProperMinAndMaxLengthValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Checks for string data type whether specified minimum length is more than maximum.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProperMinAndMaxLengthValidator.class)
@Documented
public @interface ProperMinAndMaxLength {

  String message() default Violations.STRING_MIN_LENGTH_MORE_THAN_MAX_LENGTH;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
