package com.gendb.random;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomValueProvider {

  private static final long MAX_UNSIGNED_INT = (long)(Integer.MAX_VALUE) - Integer.MIN_VALUE;

  private static final int MAX_UNSIGNED_SHORT = (int)(Short.MAX_VALUE) - Short.MIN_VALUE;

  private static final List<Character> DIGITS = new ArrayList<Character>() {{
    for (char c = '0'; c <= '9'; ++c) {
      add(c);
    }
  }};

  private static final List<Character> ALPHANUMERIC = new ArrayList<Character>() {{
    for (char c = 'a'; c <= 'z'; ++c) {
      add(c);
    }

    for (char c = 'A'; c <= 'Z'; ++c) {
      add(c);
    }

    addAll(DIGITS);
    add(' ');
  }};

  private static final int ALPHANUMERIC_CHARS_COUNT = ALPHANUMERIC.size();

  private static final long TIMESTAMP_DIFF = Instant.now().getEpochSecond() - Instant.EPOCH.getEpochSecond();

  private static final String DATE_TEMPLATE = "%1$d-01-01 00:00:00";

  private final Random rnd;

  public RandomValueProvider() {
    rnd = new Random(System.currentTimeMillis());
  }

  public RandomValueProvider(final Random rnd) {
    this.rnd = rnd;
  }

  private Long nextRandom() {
    return rnd.nextLong();
  }

  private Long nextRandom(final long min, final long max) {
    if (min == max) {
      return min;
    }

    return min + Math.abs(rnd.nextLong()) % (max - min);
  }

  private String nextRandomNumeric(final int length) {
    final boolean negative = rnd.nextBoolean();
    final int finalLength = length + (negative ? 1 : 0);
    final byte[] bytes = new byte[finalLength];
    final char[] chars = new char[finalLength];
    rnd.nextBytes(bytes);
    for (int i = 0; i < finalLength; ++i) {
      final int index = (bytes[i] - Byte.MIN_VALUE) % 10;
      chars[i] = DIGITS.get(index);
    }

    if (negative) {
      chars[0] = '-';
    }

    return new String(chars);
  }

  public short getSignedSmallint() {
    return nextRandom().shortValue();
  }

  public short getSignedSmallint(final short min, final short max) {
    return nextRandom(min, max).shortValue();
  }

  public int getUnsignedSmallint() {
    return Math.abs(nextRandom().intValue()) % MAX_UNSIGNED_SHORT;
  }

  public int getUnsignedSmallint(final short min, final short max) {
    return nextRandom(min, max).intValue();
  }

  public int getSignedInt() {
    return nextRandom().intValue();
  }

  public int getSignedInt(final int min, final int max) {
    return nextRandom(min, max).intValue();
  }

  public long getUnsignedInt() {
    return Math.abs(nextRandom()) % MAX_UNSIGNED_INT;
  }

  public long getUnsignedInt(final long min, final long max) {
    return nextRandom(min, max);
  }

  public BigDecimal getDecimal(final int precision, final int scale) {
    final BigInteger bi = new BigInteger(nextRandomNumeric(precision));
    return new BigDecimal(bi, scale);
  }

  public String getAlphanumericString(final int length) {
    final StringBuilder sb = new StringBuilder(length * 2);
    final byte[] bytes = new byte[length];
    rnd.nextBytes(bytes);
    for (int i = 0; i < length; ++i) {
      final int index = (bytes[i] - Byte.MIN_VALUE) % ALPHANUMERIC_CHARS_COUNT;
      sb.append(ALPHANUMERIC.get(index));
    }

    return sb.toString();
  }

  public long getTimestamp() {
    return Instant.EPOCH.getEpochSecond() + Math.abs(rnd.nextLong()) % TIMESTAMP_DIFF;
  }
}
