package com.gendb.random.generator.impl;

import com.gendb.random.generator.TypeGenerator;
import com.gendb.model.DataType;
import com.gendb.random.RandomValueProvider;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class DateGenerator implements TypeGenerator {

  private SimpleDateFormat sdf;

  private RandomValueProvider provider;

  @Override
  public void init(DataType type, RandomValueProvider provider) {
    this.provider = provider;
    sdf = new SimpleDateFormat("YYYY-MM-dd");
  }

  @Override
  public Object yield() {
    final long timestamp = provider.getTimestamp();
    final Date date = Date.from(Instant.ofEpochSecond(timestamp));
    return sdf.format(date);
  }
}
