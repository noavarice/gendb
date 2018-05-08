package com.gendb.random.generator.impl;

import com.gendb.random.generator.TypeGenerator;
import com.gendb.model.DataType;
import com.gendb.model.wrapper.ValueWrapper;
import com.gendb.model.wrapper.impl.StringDateWrapper;
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
  public ValueWrapper yield() {
    final Date date = Date.from(Instant.ofEpochSecond(provider.getTimestamp()));
    return new StringDateWrapper(sdf.format(date));
  }
}
