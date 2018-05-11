package com.gendb.generation.generator.impl;

import com.gendb.generation.GenerationContext;
import com.gendb.generation.RandomValueProvider;
import com.gendb.generation.generator.TypeGenerator;
import com.gendb.model.pure.DataType;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class DateGenerator implements TypeGenerator {

  private SimpleDateFormat sdf;

  private RandomValueProvider provider;

  @Override
  public void init(final DataType type, RandomValueProvider provider) {
    this.provider = provider;
    sdf = new SimpleDateFormat("YYYY-MM-dd");
  }

  @Override
  public Object yield(final GenerationContext context) {
    final long timestamp = provider.getTimestamp();
    final Date date = Date.from(Instant.ofEpochSecond(timestamp));
    return sdf.format(date);
  }
}
