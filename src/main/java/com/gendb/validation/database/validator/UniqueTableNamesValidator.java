package com.gendb.validation.database.validator;

import com.gendb.model.validating.ValidatingTable;
import com.gendb.validation.database.UniqueTableNames;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueTableNamesValidator implements ConstraintValidator<UniqueTableNames, List<ValidatingTable>> {

  @Override
  public boolean isValid(final List<ValidatingTable> tables, final ConstraintValidatorContext context) {
    final List<String> names = tables.stream().map(ValidatingTable::getName).collect(Collectors.toList());
    final Set<String> uniqueNames = new HashSet<>(names);
    return names.size() == uniqueNames.size();
  }
}
