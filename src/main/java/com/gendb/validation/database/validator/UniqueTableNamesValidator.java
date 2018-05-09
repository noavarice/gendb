package com.gendb.validation.database.validator;

import com.gendb.model.Table;
import com.gendb.validation.database.UniqueTableNames;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueTableNamesValidator implements ConstraintValidator<UniqueTableNames, List<Table>> {

  @Override
  public boolean isValid(final List<Table> tables, final ConstraintValidatorContext context) {
    final List<String> names = tables.stream().map(Table::getName).collect(Collectors.toList());
    final Set<String> uniqueNames = new HashSet<>(names);
    return names.size() == uniqueNames.size();
  }
}
