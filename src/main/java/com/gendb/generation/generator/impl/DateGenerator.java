package com.gendb.generation.generator.impl;

import com.gendb.generation.GenerationContext;
import com.gendb.generation.RandomValueProvider;
import com.gendb.generation.generator.TypeGenerator;
import com.gendb.model.pure.DataType;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateGenerator implements TypeGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(DateGenerator.class);

  private SimpleDateFormat sdf;

  private RandomValueProvider provider;

  private String minColumn;

  @Override
  public void init(final DataType type, RandomValueProvider provider) {
    minColumn = type.getMinColumn() == null ? null : type.getMinColumn().getName();
    this.provider = provider;
    sdf = new SimpleDateFormat("YYYY-MM-dd");
  }

  @Override
  public Object yield(final GenerationContext context) {
    final Object minColumnValue = context.getValue(minColumn);
    Timestamp timestamp;
    if (minColumnValue == null) {
      timestamp = provider.getTimestamp();
    } else {
      try {
        final long minTimestamp = sdf.parse((String)minColumnValue).getTime() / 1000;
        timestamp = provider.getTimestamp(minTimestamp);
      } catch (ParseException e) {
        LOGGER.error("Incorrect date format string received: {}", minColumnValue);
        return null;
      }
    }

    return sdf.format(timestamp.toLocalDateTime());
  }
}
