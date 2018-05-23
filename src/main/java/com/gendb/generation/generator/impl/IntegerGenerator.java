package com.gendb.generation.generator.impl;

import com.gendb.generation.GenerationContext;
import com.gendb.generation.RandomValueProvider;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntegerGenerator implements TypeGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(IntegerGenerator.class);

  private String minColumn;

  private List<ConcreteDistributionInterval> distribution;

  @Override
  public void init(final Column column) {
    final DataType type = column.getType();
    minColumn = type.getMinColumn() == null ? null : type.getMinColumn().getName();
    final PureModelMapper mapper = MapperUtils.getMapper(PureModelMapper.class);
    final int rowsCount = column.getTable().getRowsCount();
    if (column.getDistributionIntervals().isEmpty()) {
      final long min = (long)(type.getMin() == null ? TypeUtils.getMinValue(type.getName()) : type.getMin());
      final long max = (long)(type.getMax() == null ? TypeUtils.getMaxValue(type.getName()) : type.getMax());
      distribution = new ArrayList<>(Collections.singletonList(new ConcreteDistributionInterval(min, max, rowsCount)));
      return;
    }

    distribution = mapper.mapDistribution(column.getDistributionIntervals(), rowsCount);
  }

  @Override
  public Object yield(final GenerationContext context) {
    final RandomValueProvider provider = context.getRandom();
    final Object minColumnValue = context.getValue(minColumn);
    final int index = (int)provider.getNumber(0, distribution.size() - 1);
    final ConcreteDistributionInterval interval = distribution.get(index);
    final Long min = minColumnValue == null ? interval.getMin() : (Long)minColumnValue;
    final long result = provider.getNumber(min, interval.getMax());
    if (interval.getCount() == 1) {
      distribution.remove(index);
    } else {
      interval.setCount(interval.getCount() - 1);
    }

    return result;
  }
}
