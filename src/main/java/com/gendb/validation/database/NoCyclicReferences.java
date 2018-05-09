package com.gendb.validation.database;

import com.gendb.validation.Violations;
import com.gendb.validation.database.validator.NoCyclicReferencesValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoCyclicReferencesValidator.class)
@Documented
public @interface NoCyclicReferences {

  String message() default Violations.NO_CYCLIC_REFERENCES;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
