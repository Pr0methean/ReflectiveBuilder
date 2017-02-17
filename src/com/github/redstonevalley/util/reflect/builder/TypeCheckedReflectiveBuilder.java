package com.github.redstonevalley.util.reflect.builder;

import java.lang.reflect.Field;

import javax.annotation.Nullable;

public abstract class TypeCheckedReflectiveBuilder<T> extends ReflectiveBuilder<T> {

  public TypeCheckedReflectiveBuilder(Class<T> clazz) {
    super(clazz, false);
  }

  /**
   * {@inheritDoc}
   * 
   * @throws SecurityException if the field to be set cannot be made accessible.
   */
  @Override
  public void setField(Field field, @Nullable Object value) {
    field.setAccessible(true);
    super.setField(field, value);
  }

  /**
   * Sets the given field to the given value. The type of {@code value} has
   * already been checked if necessary.
   *
   * @param objectBeingBuilt The object under construction.
   * @param field The field to set.
   * @param value The value to set the field to.
   */
  protected void setFieldInInstance(T objectBeingBuilt, Field field, @Nullable Object value) {
    try {
      field.set(objectBeingBuilt, value);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected Object getField(Object sourceObject, Field field) {
    synchronized (sourceObject) {
      field.setAccessible(true);
      try {
        return field.get(sourceObject);
      } catch (IllegalArgumentException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public T build() {
    T built = allocateInstance();
    synchronized (this) {
      for (Field f : fieldValues.keySet()) {
        setFieldInInstance(built, f, fieldValues.get(f));
      }
      for (Field f : explicitlyNullFields) {
        setFieldInInstance(built, f, null);
      }
    }
    return built;
  }

  /**
   * @return The instance of {@link #clazz} whose fields will be set by
   *         {@link #build()} and then returned.
   */
  protected abstract T allocateInstance();
}
