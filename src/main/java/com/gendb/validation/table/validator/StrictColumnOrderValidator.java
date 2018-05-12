package com.gendb.validation.table.validator;

import com.gendb.model.validating.ValidatingTable;
import com.gendb.model.validating.ValidatingValueOrder;
import com.gendb.validation.ValidationUtils;
import com.gendb.validation.table.StrictColumnOrder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StrictColumnOrderValidator implements ConstraintValidator<StrictColumnOrder, ValidatingTable> {

  private static <T> boolean before(final List<T> list, final T obj1, final T obj2) {
    return list.indexOf(obj1) < list.indexOf(obj2);
  }

  @Override
  public boolean isValid(final ValidatingTable table, final ConstraintValidatorContext context) {
    final List<ValidatingValueOrder> allOrders = table.getValueOrders();
    if (allOrders.isEmpty()) {
      return true;
    }

    final List<String> columns = allOrders.stream()
      .map(ValidatingValueOrder::getColumns)
      .flatMap(List::stream)
      .distinct()
      .collect(Collectors.toList());
    for (final String col1: columns) {
      for (final String col2: columns) {
        if (col1.equals(col2)) {
          continue;
        }

        final List<String> pair = Arrays.asList(col1, col2);
        final List<List<String>> orders = allOrders.stream()
          .map(ValidatingValueOrder::getColumns)
          .filter(l -> l.containsAll(pair))
          .collect(Collectors.toList());
        if (orders.stream().anyMatch(l -> before(l, col1, col2)) && orders.stream().anyMatch(l -> before(l, col2, col1))) {
          ValidationUtils.addViolation(context, col1, col2);
          return false;
        }
      }
    }

    return true;
  }
}
