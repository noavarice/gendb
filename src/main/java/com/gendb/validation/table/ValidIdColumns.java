package com.gendb.validation.table;

import com.gendb.validation.Violations;
import com.gendb.validation.table.impl.ValidIdColumnsValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidIdColumnsValidator.class)
@Documented
public @interface ValidIdColumns {

  String message() default Violations.UNKNOWN_ID_COLUMN;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
