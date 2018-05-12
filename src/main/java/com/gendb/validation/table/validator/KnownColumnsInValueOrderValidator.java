package com.gendb.validation.table.validator;

import com.gendb.model.validating.ValidatingColumn;
import com.gendb.model.validating.ValidatingTable;
import com.gendb.model.validating.ValidatingValueOrder;
import com.gendb.validation.ValidationUtils;
import com.gendb.validation.table.KnownColumnsInValueOrder;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class KnownColumnsInValueOrderValidator implements ConstraintValidator<KnownColumnsInValueOrder, ValidatingTable> {

  @Override
  public boolean isValid(final ValidatingTable table, final ConstraintValidatorContext context) {
    if (table.getValueOrders().isEmpty()) {
      return true;
    }

    final Set<String> validColumns = table.getColumns().stream()
      .map(ValidatingColumn::getName)
      .collect(Collectors.toSet());
    final List<String> checkingColumns = table.getValueOrders().stream()
      .map(ValidatingValueOrder::getColumns)
      .flatMap(List::stream)
      .collect(Collectors.toList());
    if (!validColumns.containsAll(checkingColumns)) {
      ValidationUtils.addViolation(context, table.getName());
      return false;
    }

    return true;
  }
}
