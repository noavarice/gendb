package com.gendb.generation.generator.impl;

import com.gendb.exception.IncorrectTypeException;
import com.gendb.generation.GenerationContext;
import com.gendb.generation.RandomProvider;
import com.gendb.generation.generator.TypeGenerator;
import com.gendb.mapper.PureModelMapper;
import com.gendb.model.pure.Column;
import com.gendb.model.pure.ConcreteDistributionInterval;
import com.gendb.model.pure.DataType;
import com.gendb.util.MapperUtils;
import com.gendb.util.TypeUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FloatingGenerator implements TypeGenerator {

  private String minColumn;

  private List<ConcreteDistributionInterval> distribution;

  @Override
  public void init(final Column column) throws IncorrectTypeException {
    final DataType type = column.getType();
    minColumn = type.getMinColumn() == null ? null : type.getMinColumn().getName();
    final PureModelMapper mapper = MapperUtils.getMapper(PureModelMapper.class);
    final int rowsCount = column.getTable().getRowsCount();
    if (column.getDistributionIntervals().isEmpty()) {
      final double min = type.getMin() == null ? TypeUtils.getMinValue(type.getName()) : type.getMin();
      final double max = type.getMax() == null ? TypeUtils.getMaxValue(type.getName()) : type.getMax();
      final ConcreteDistributionInterval interval = new ConcreteDistributionInterval(min, max, rowsCount);
      distribution = new ArrayList<>(Collections.singletonList(interval));
      return;
    }

    distribution = mapper.mapDistribution(column.getDistributionIntervals(), rowsCount);
  }

  @Override
  public Object yield(GenerationContext context) {
    final RandomProvider provider = context.getRandom();
    final Object minColumnValue = context.getValue(minColumn);
    final int index = (int)provider.getNumber(0, distribution.size() - 1);
    final ConcreteDistributionInterval interval = distribution.get(index);
    final double min = minColumnValue == null ? interval.getMin() : (Double)minColumnValue;
    final double result = provider.getNumber(min, interval.getMax());
    if (interval.getCount() == 1) {
      distribution.remove(index);
    } else {
      interval.setCount(interval.getCount() - 1);
    }

    return result;
  }
}
