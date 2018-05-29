package com.gendb.validation.type;

import com.gendb.validation.Violations;
import com.gendb.validation.type.validator.StringPropertiesPresentValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Checks for string data types that length attributes are properly set.
 * One of two cases are allowed: specified single 'length' attribute, so having generated strings
 * with fixed length, or 'length' attribute is missed and both 'minLength' and 'maxLength'
 * attribute values are set.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StringPropertiesPresentValidator.class)
@Documented
public @interface StringPropertiesPresent {

  String message() default Violations.STRING_PROPERTIES_MISSED;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
