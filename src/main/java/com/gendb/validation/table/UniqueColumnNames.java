package com.gendb.validation.table;

import com.gendb.validation.Violations;
import com.gendb.validation.table.validator.UniqueColumnNamesValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueColumnNamesValidator.class)
@Documented
public @interface UniqueColumnNames {

  String message() default Violations.NON_UNIQUE_COLUMN_NAMES;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
