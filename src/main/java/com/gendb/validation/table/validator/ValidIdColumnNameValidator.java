package com.gendb.validation.table.validator;

import com.gendb.model.Column;
import com.gendb.model.Table;
import com.gendb.validation.ValidationUtils;
import com.gendb.validation.table.ValidIdColumnName;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Checks that name of ID table column is not used by another columns
 */
public class ValidIdColumnNameValidator implements ConstraintValidator<ValidIdColumnName, Table> {

  @Override
  public boolean isValid(final Table table, final ConstraintValidatorContext context) {
    final String idColumnName = table.getIdColumnName();
    final List<String> tableColumns = table.getColumns().stream().map(Column::getName).collect(Collectors.toList());
    final boolean result = !tableColumns.contains(idColumnName);
    if (!result) {
      ValidationUtils.addViolation(context, table.getName());
    }

    return result;
  }
}
