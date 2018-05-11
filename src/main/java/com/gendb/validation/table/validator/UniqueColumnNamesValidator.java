package com.gendb.validation.table.validator;

import com.gendb.model.validating.ValidatingColumn;
import com.gendb.model.validating.ValidatingTable;
import com.gendb.validation.ValidationUtils;
import com.gendb.validation.table.UniqueColumnNames;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueColumnNamesValidator implements ConstraintValidator<UniqueColumnNames, ValidatingTable> {

  @Override
  public boolean isValid(ValidatingTable table, ConstraintValidatorContext context) {
    final List<String> columnNames = table.getColumns().stream()
      .map(ValidatingColumn::getName)
      .collect(Collectors.toList());
    columnNames.add(table.getIdColumnName());
    final Set<String> uniqueNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    uniqueNames.addAll(columnNames);
    if (columnNames.size() != uniqueNames.size()) {
      ValidationUtils.addViolation(context, table.getName());
      return false;
    }

    return true;
  }
}
