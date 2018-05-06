package com.gendb.handler.impl;

import com.gendb.handler.TypeHandler;
import com.gendb.model.DataType;
import com.gendb.random.RandomValueProvider;
import java.util.function.LongSupplier;

public class IntegerHandler implements TypeHandler {

  private LongSupplier f;

  @Override
  public void init(final DataType type, final RandomValueProvider provider) {
    if (type.isUnsigned()) {
      if (type.getMin() == null && type.getMax() == null) {
        f = provider::getUnsignedInt;
        return;
      }

      final int min = type.getMin() == null ? 0 : type.getMin().intValue();
      final int max = type.getMax() == null ? 0 : type.getMax().intValue();
      f = () -> provider.getUnsignedInt(min, max);
      return;
    }

    if (type.getMin() == null && type.getMax() == null) {
      f = provider::getSignedInt;
      return;
    }

    final int min = type.getMin() == null ? 0 : type.getMin().intValue();
    final int max = type.getMax() == null ? 0 : type.getMax().intValue();
    f = () -> provider.getSignedInt(min, max);
  }

  @Override
  public Object yield() {
    return f.getAsLong();
  }
}
