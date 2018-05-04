package com.gendb.validation.type;

import com.gendb.validation.Violations;
import com.gendb.validation.type.validator.DecimalPropertiesPresentValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DecimalPropertiesPresentValidator.class)
@Documented
public @interface DecimalPropertiesPresent {

  String message() default Violations.DECIMAL_PROPERTIES_MISSED;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
