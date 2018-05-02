package com.gendb.validation.table.impl;

import com.gendb.model.Column;
import com.gendb.model.Table;
import com.gendb.validation.table.ValidIdColumns;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Performs check upon {@link Table} that every column listed in {@link Table#id}
 * is present as part of {@link Table#columns} collection
 */
public class ValidIdColumnsValidator implements ConstraintValidator<ValidIdColumns, Table> {

  @Override
  public boolean isValid(final Table table, final ConstraintValidatorContext context) {
    final List<String> idColumns = table.getId();
    final List<String> tableColumns = table.getColumns().stream().map(Column::getName).collect(Collectors.toList());
    final boolean result = tableColumns.containsAll(idColumns);
    if (!result) {
      final String template = context.getDefaultConstraintMessageTemplate();
      context.buildConstraintViolationWithTemplate(String.format(template, table.getName()))
        .addConstraintViolation()
        .disableDefaultConstraintViolation();
    }

    return result;
  }
}
