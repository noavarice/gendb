package com.gendb.validation.table.validator;

import com.gendb.model.validating.ValidatingTable;
import com.gendb.model.validating.ValidatingValueOrder;
import com.gendb.validation.ValidationUtils;
import com.gendb.validation.table.DuplicateColumnsInValueOrder;
import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DuplicateColumnsInValueOrderValidator implements ConstraintValidator<DuplicateColumnsInValueOrder, ValidatingTable> {

  @Override
  public boolean isValid(final ValidatingTable table, final ConstraintValidatorContext context) {
    if (table.getValueOrders().isEmpty()) {
      return true;
    }

    for (final ValidatingValueOrder order: table.getValueOrders()) {
      final Set<String> distinctColumns = new HashSet<>(order.getColumns());
      if (distinctColumns.size() != order.getColumns().size()) {
        ValidationUtils.addViolation(context, table.getName());
        return false;
      }
    }

    return true;
  }
}
