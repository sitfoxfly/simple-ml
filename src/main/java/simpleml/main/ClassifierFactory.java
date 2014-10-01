package simpleml.main;

import simpleml.classify.*;
import simpleml.classify.ext.ExternalizableModel;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * @author mtkachenko
 */
public enum ClassifierFactory {
  INSTANCE;

  private final List<? extends Class<? extends Trainable>> classifiers = Arrays.asList(
      LinearPerceptron.class,
      AveragedLinearPerceptron.class,
      PassiveAggressivePerceptron.class,
      PegasosSVM.class
  );

  @SuppressWarnings("unchecked")
  public Class<? extends ExternalizableModel> getModelClass(int type) {
    return (Class<? extends ExternalizableModel>) classifiers.get(type);
  }

  public Trainable build(int type, int dimension) {
    final Class<? extends Trainable> clazz = classifiers.get(type);
    try {
      return clazz.getConstructor(int.class).newInstance(dimension);
    } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

}
