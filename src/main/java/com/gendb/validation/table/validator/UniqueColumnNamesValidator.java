package com.gendb.validation.table.validator;

import com.gendb.model.Column;
import com.gendb.model.Table;
import com.gendb.validation.ValidationUtils;
import com.gendb.validation.table.UniqueColumnNames;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueColumnNamesValidator implements ConstraintValidator<UniqueColumnNames, Table> {

  @Override
  public boolean isValid(Table table, ConstraintValidatorContext context) {
    final List<String> columnNames = table.getColumns().stream().map(Column::getName).collect(Collectors.toList());
    final Set<String> names = new HashSet<>();
    for (final String name: columnNames) {
      if (!names.add(name)) {
        ValidationUtils.addViolation(context, table.getName());
        return false;
      }
    }

    return true;
  }
}
