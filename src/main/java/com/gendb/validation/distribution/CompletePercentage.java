package com.gendb.validation.distribution;

import com.gendb.validation.Violations;
import com.gendb.validation.distribution.validator.CompletePercentageValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CompletePercentageValidator.class)
@Documented
public @interface CompletePercentage {

  String message() default Violations.INCOMPLETE_DISTRIBUTION_PERCENTAGE;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
