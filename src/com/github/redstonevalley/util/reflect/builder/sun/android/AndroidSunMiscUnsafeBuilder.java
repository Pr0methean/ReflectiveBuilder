package com.github.redstonevalley.util.reflect.builder.sun.android;

import com.github.redstonevalley.util.reflect.builder.sun.SunMiscUnsafeBuilder;

/**
 * A {@link SunMiscUnsafeBuilder} that needs only those methods which Android's version of
 * {@link sun.misc.Unsafe} provides.
 */
@SuppressWarnings("restriction")
public class AndroidSunMiscUnsafeBuilder<T> extends SunMiscUnsafeBuilder<T> {

  public AndroidSunMiscUnsafeBuilder(Class<T> clazz) {
    super(clazz);
  }

  @Override
  protected void putDouble(T objectBeingBuilt, long fieldOffset, double value) {
    UNSAFE.putLong(objectBeingBuilt, fieldOffset, Double.doubleToRawLongBits(value));
  }

  @Override
  protected void putFloat(T objectBeingBuilt, long fieldOffset, float value) {
    UNSAFE.putInt(objectBeingBuilt, fieldOffset, Float.floatToRawIntBits(value));
  }

  @Override
  protected void putChar(T objectBeingBuilt, long fieldOffset, char value) {
    int oldValue = UNSAFE.getInt(objectBeingBuilt, fieldOffset - 2);
    int newValue = (oldValue & 0xFFFF0000) | value;
    UNSAFE.putInt(objectBeingBuilt, fieldOffset - 2, newValue);
  }

  @Override
  protected void putShort(T objectBeingBuilt, long fieldOffset, short value) {
    int oldValue = UNSAFE.getInt(objectBeingBuilt, fieldOffset - 2);
    int newValue = (oldValue & 0xFFFF0000) | value;
    UNSAFE.putInt(objectBeingBuilt, fieldOffset - 2, newValue);
  }

  @Override
  protected void putByte(T objectBeingBuilt, long fieldOffset, byte value) {
    int oldValue = UNSAFE.getInt(objectBeingBuilt, fieldOffset - 3);
    int newValue = (oldValue & 0xFFFFFF00) | value;
    UNSAFE.putInt(objectBeingBuilt, fieldOffset - 3, newValue);
  }

  @Override
  protected void putBoolean(T objectBeingBuilt, long fieldOffset, boolean value) {
    int oldValue = UNSAFE.getInt(objectBeingBuilt, fieldOffset - 3);
    int newValue = (oldValue & 0xFFFFFF00) | (value ? 1 : 0);
    UNSAFE.putInt(objectBeingBuilt, fieldOffset - 3, newValue);
  }

  @Override
  protected Object getDouble(Object sourceObject, long fieldOffset) {
    return Double.longBitsToDouble(UNSAFE.getLong(sourceObject, fieldOffset));
  }

  @Override
  protected Object getFloat(Object sourceObject, long fieldOffset) {
    return Float.intBitsToFloat(UNSAFE.getInt(sourceObject, fieldOffset));
  }

  @Override
  protected Object getChar(Object sourceObject, long fieldOffset) {
    // TODO Auto-generated method stub
    return (char) (UNSAFE.getInt(sourceObject, fieldOffset - 2) & 0x0000FFFF);
  }

  @Override
  protected Object getShort(Object sourceObject, long fieldOffset) {
    // TODO Auto-generated method stub
    return (short) (UNSAFE.getInt(sourceObject, fieldOffset - 2) & 0x0000FFFF);
  }

  @Override
  protected Object getByte(Object sourceObject, long fieldOffset) {
    // TODO Auto-generated method stub
    return (byte) (UNSAFE.getInt(sourceObject, fieldOffset - 3) & 0x000000FF);
  }

  @Override
  protected Object getBoolean(Object sourceObject, long fieldOffset) {
    // TODO Auto-generated method stub
    return (UNSAFE.getInt(sourceObject, fieldOffset - 3) & 0x000000FF) > 0;
  }

}
