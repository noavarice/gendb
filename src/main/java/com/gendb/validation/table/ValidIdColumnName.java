package com.gendb.validation.table;

import com.gendb.validation.Violations;
import com.gendb.validation.table.validator.ValidIdColumnNameValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidIdColumnNameValidator.class)
@Documented
public @interface ValidIdColumnName {

  String message() default Violations.ID_COLUMN_NAME_ALREADY_USED;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
