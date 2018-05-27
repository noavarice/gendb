package com.gendb.validation.type;

import com.gendb.validation.Violations;
import com.gendb.validation.type.validator.PrecisionLessThanScaleValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PrecisionLessThanScaleValidator.class)
@Documented
public @interface PrecisionLessThanScale {

  String message() default Violations.PRECISION_NOT_LESS_THAN_SCALE;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
