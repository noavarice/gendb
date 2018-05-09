package com.gendb.validation.table.validator;

import com.gendb.model.Column;
import com.gendb.model.ForeignKey;
import com.gendb.model.Table;
import com.gendb.validation.ValidationUtils;
import com.gendb.validation.table.UniqueColumnNames;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueColumnNamesValidator implements ConstraintValidator<UniqueColumnNames, Table> {

  @Override
  public boolean isValid(Table table, ConstraintValidatorContext context) {
    final List<String> columnNames = table.getColumns().stream()
      .map(Column::getName)
      .collect(Collectors.toList());
    columnNames.addAll(table.getForeignKeys().stream()
      .map(ForeignKey::getColumnName)
      .collect(Collectors.toList()));
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
