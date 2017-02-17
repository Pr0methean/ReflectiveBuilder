package com.github.redstonevalley.util.reflect.builder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.annotation.Nullable;

/**
 * A {@link TypeCheckedReflectiveBuilder} that invokes the specified constructor and can be
 * configured with the constructor's parameters.
 */
public class ConstructingBuilder<T> extends TypeCheckedReflectiveBuilder<T> {

  protected final Constructor<T> constructor;
  protected final Class<?>[] ctorParamTypes;
  protected final Object[] ctorParams;

  public ConstructingBuilder(Class<T> clazz, Constructor<T> constructor) {
    super(clazz);
    this.constructor = constructor;
    ctorParamTypes = constructor.getParameterTypes();
    ctorParams = new Object[ctorParamTypes.length];
  }

  public void setConstructorParam(int index, @Nullable Object value) {
    if (!isReallyAssignableFrom(ctorParamTypes[index], value)) {
      throw new ClassCastException(
          String.format(
              "%s cannot be cast to %s for parameter %d of %s",
              value,
              ctorParamTypes[index],
              index,
              constructor));
    }
    ctorParams[index] = value;
  }

  @Override
  protected T allocateInstance() {
    try {
      return constructor.newInstance((Object[]) ctorParams);
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException e)
    {
      throw new RuntimeException(e);
    }
  }
}
