package com.github.redstonevalley.util.reflect.builder.objenesis;

import org.objenesis.instantiator.ObjectInstantiator;

import com.github.redstonevalley.util.reflect.builder.TypeCheckedReflectiveBuilder;

/**
 * A {@link TypeCheckedReflectiveBuilder} that uses an {@link ObjectInstantiator}. This makes it
 * possible on most JVMs to build an object of any class without running a constructor.
 *
 * @author cryoc
 *
 * @param <T>
 */
public class ObjenesisBuilder<T> extends TypeCheckedReflectiveBuilder<T> {

  protected final ObjectInstantiator<T> instantiator;
  
  public ObjenesisBuilder(Class<T> clazz, ObjectInstantiator<T> instantiator) {
    super(clazz);
    this.instantiator = instantiator;
  }

  @Override
  protected T allocateInstance() {
    return instantiator.newInstance();
  }
}
