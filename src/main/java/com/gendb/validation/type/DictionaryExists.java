package com.gendb.validation.type;

import com.gendb.validation.Violations;
import com.gendb.validation.type.validator.DictionaryExistsValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DictionaryExistsValidator.class)
@Documented
public @interface DictionaryExists {

  String message() default Violations.DICTIONARY_NOT_EXISTS;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
