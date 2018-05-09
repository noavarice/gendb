package com.gendb.validation.database;

import com.gendb.validation.Violations;
import com.gendb.validation.database.validator.UniqueTableNamesValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueTableNamesValidator.class)
@Documented
public @interface UniqueTableNames {

  String message() default Violations.NON_UNIQUE_TABLE_NAMES;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
