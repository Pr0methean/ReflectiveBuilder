package com.github.redstonevalley.util.reflect.builder.sun.oracle;

import com.github.redstonevalley.util.reflect.builder.sun.SunMiscUnsafeBuilder;

/**
 * A {@link SunMiscUnsafeBuilder} that uses the Oracle or OpenJDK implementation
 * of {@link sun.misc.Unsafe} to keep its methods as simple as possible.
 *
 * @author cryoc
 *
 * @param <T>
 */
@SuppressWarnings("restriction")
public class OracleSunMiscUnsafeBuilder<T> extends SunMiscUnsafeBuilder<T> {

  public OracleSunMiscUnsafeBuilder(Class<T> clazz) {
    super(clazz);
  }

  @Override
  protected void putDouble(T objectBeingBuilt, long fieldOffset, double value) {
    UNSAFE.putDouble(objectBeingBuilt, fieldOffset, value);
  }

  @Override
  protected void putFloat(T objectBeingBuilt, long fieldOffset, float value) {
    UNSAFE.putFloat(objectBeingBuilt, fieldOffset, value);
  }

  @Override
  protected void putChar(T objectBeingBuilt, long fieldOffset, char value) {
    UNSAFE.putChar(objectBeingBuilt, fieldOffset, value);
  }

  @Override
  protected void putShort(T objectBeingBuilt, long fieldOffset, short value) {
    UNSAFE.putShort(objectBeingBuilt, fieldOffset, value);
  }

  @Override
  protected void putByte(T objectBeingBuilt, long fieldOffset, byte value) {
    UNSAFE.putByte(objectBeingBuilt, fieldOffset, value);
  }

  @Override
  protected void putBoolean(T objectBeingBuilt, long fieldOffset, boolean value) {
    UNSAFE.putBoolean(objectBeingBuilt, fieldOffset, value);
  }

  @Override
  protected Object getDouble(Object sourceObject, long fieldOffset) {
    return UNSAFE.getDouble(sourceObject, fieldOffset);
  }

  @Override
  protected Object getFloat(Object sourceObject, long fieldOffset) {
    return UNSAFE.getFloat(sourceObject, fieldOffset);
  }

  @Override
  protected Object getChar(Object sourceObject, long fieldOffset) {
    return UNSAFE.getChar(sourceObject, fieldOffset);
  }

  @Override
  protected Object getShort(Object sourceObject, long fieldOffset) {
    return UNSAFE.getShort(sourceObject, fieldOffset);
  }

  @Override
  protected Object getByte(Object sourceObject, long fieldOffset) {
    return UNSAFE.getByte(sourceObject, fieldOffset);
  }

  @Override
  protected Object getBoolean(Object sourceObject, long fieldOffset) {
    return UNSAFE.getBoolean(sourceObject, fieldOffset);
  }

}
