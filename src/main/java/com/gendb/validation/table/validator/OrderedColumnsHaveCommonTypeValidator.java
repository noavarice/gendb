package com.gendb.validation.table.validator;

import com.gendb.model.validating.ValidatingColumn;
import com.gendb.model.validating.ValidatingDataType;
import com.gendb.model.validating.ValidatingTable;
import com.gendb.model.validating.ValidatingValueOrder;
import com.gendb.validation.ValidationUtils;
import com.gendb.validation.table.OrderedColumnsHaveCommonType;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OrderedColumnsHaveCommonTypeValidator implements ConstraintValidator<OrderedColumnsHaveCommonType, ValidatingTable> {

  @Override
  public boolean isValid(final ValidatingTable table, final ConstraintValidatorContext context) {
    if (table.getValueOrders().isEmpty()) {
      return true;
    }

    final Map<String, ValidatingColumn> nameToColumn = table.getColumns().stream()
      .collect(Collectors.toMap(ValidatingColumn::getName, Function.identity()));

    for (final ValidatingValueOrder order: table.getValueOrders()) {
      final List<ValidatingColumn> columns = order.getColumns().stream()
        .map(nameToColumn::get)
        .collect(Collectors.toList());
      final List<String> types = columns.stream()
        .map(ValidatingColumn::getType)
        .map(ValidatingDataType::getName)
        .distinct()
        .collect(Collectors.toList());
      if (types.size() == 1) {
        continue;
      }

      final StringJoiner sj = new StringJoiner(",");
      for (final String type: types) {
        final ValidatingColumn example = columns.stream()
          .filter(c -> c.getType().getName().equals(type))
          .findFirst()
          .get();
        sj.add(String.format("'%1$s' (%2$s)", example.getName(), example.getType().getName()));
      }

      ValidationUtils.addViolation(context, table.getName(), sj.toString());
      return false;
    }

    return true;
  }
}
