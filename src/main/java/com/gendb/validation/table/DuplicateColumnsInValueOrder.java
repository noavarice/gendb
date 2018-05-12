package com.gendb.validation.table;

import com.gendb.validation.Violations;
import com.gendb.validation.table.validator.DuplicateColumnsInValueOrderValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DuplicateColumnsInValueOrderValidator.class)
@Documented
public @interface DuplicateColumnsInValueOrder {

  String message() default Violations.DUPLICATE_COLUMNS_IN_VALUE_ORDER;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
