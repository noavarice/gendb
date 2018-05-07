package com.gendb.handler.impl;

import com.gendb.handler.TypeHandler;
import com.gendb.model.DataType;
import com.gendb.model.wrapper.DefaultWrapper;
import com.gendb.model.wrapper.impl.StringDateWrapper;
import com.gendb.random.RandomValueProvider;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHandler implements TypeHandler {

  private SimpleDateFormat sdf;

  private RandomValueProvider provider;

  @Override
  public void init(DataType type, RandomValueProvider provider) {
    this.provider = provider;
    sdf = new SimpleDateFormat("YYYY-MM-dd");
  }

  @Override
  public DefaultWrapper yield() {
    return new StringDateWrapper(sdf.format(new Date(provider.getTimeStamp())));
  }
}
