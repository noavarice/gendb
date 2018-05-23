package com.gendb.validation.distribution;

import com.gendb.validation.Violations;
import com.gendb.validation.distribution.validator.OrderedPointsValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OrderedPointsValidator.class)
@Documented
public @interface OrderedPoints {

  String message() default Violations.UNORDERED_DISTRIBUTION_POINTS;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
