package com.gendb.validation.type.validator;

import com.gendb.model.validating.ValidatingDataType;
import com.gendb.validation.ValidationUtils;
import com.gendb.validation.type.DictionaryExists;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DictionaryExistsValidator implements ConstraintValidator<DictionaryExists, ValidatingDataType> {

  @Override
  public boolean isValid(final ValidatingDataType type, final ConstraintValidatorContext context) {
    final String dictionary = type.getDictionary();
    if (dictionary == null) {
      return true;
    }

    if (Files.exists(Paths.get(dictionary), LinkOption.NOFOLLOW_LINKS)) {
      return true;
    }

    ValidationUtils.addViolation(context, dictionary);
    return false;
  }
}
