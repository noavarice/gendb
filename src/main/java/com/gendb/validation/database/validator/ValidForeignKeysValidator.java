package com.gendb.validation.database.validator;

import com.gendb.model.ForeignKey;
import com.gendb.model.Table;
import com.gendb.validation.ValidationUtils;
import com.gendb.validation.database.ValidForeignKeys;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidForeignKeysValidator implements ConstraintValidator<ValidForeignKeys, List<Table>> {

  @Override
  public boolean isValid(final List<Table> tables, final ConstraintValidatorContext context) {
    final Set<String> tableNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    tableNames.addAll(tables.stream().map(Table::getName).collect(Collectors.toList()));
    for (final Table t: tables) {
      for (final ForeignKey fk: t.getForeignKeys()) {
        if (!tableNames.contains(fk.getTargetTable())) {
          ValidationUtils.addViolation(context, t.getName(), fk.getTargetTable());
          return false;
        }
      }
    }

    return true;
  }
}
