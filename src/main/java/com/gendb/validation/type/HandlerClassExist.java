package com.gendb.validation.type;

import com.gendb.validation.Violations;
import com.gendb.validation.type.validator.HandlerClassExistValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HandlerClassExistValidator.class)
@Documented
public @interface HandlerClassExist {

  String message() default Violations.HANDLER_CLASS_NOT_FOUND;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
