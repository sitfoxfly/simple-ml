package org.simpleml.classify.ext;

import org.junit.Assert;
import org.junit.Test;
import org.simpleml.classify.*;
import org.simpleml.struct.LabeledVector;
import org.simpleml.struct.Vector;
import org.simpleml.utils.VectorUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author sitfoxfly
 */
public class SaveAndLoadTest {

  private void testLinear(Trainable trainable) throws IOException, LoadException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    final Method getWeights = trainable.getClass().getMethod("getWeights");
    final List<LabeledVector> trainingData = DataUtils.getSimpleTrainingData();
    trainable.train(trainingData);
    final ExternalizableModel loadedModel = ExtUtil.trySaveAndLoad((ExternalizableModel) trainable);

    final Vector v1 = (Vector) getWeights.invoke(trainable);
    final Vector v2 = (Vector) getWeights.invoke(loadedModel);
    Assert.assertTrue(VectorUtils.equals(v1, v2));
  }

  @Test
  public void testSaveAndLoadLinear() throws Exception {
    testLinear(new AveragedLinearPerceptron(DataUtils.DATA_SIZE));
    testLinear(new LinearPerceptron(DataUtils.DATA_SIZE));
    testLinear(new PassiveAggressivePerceptron(DataUtils.DATA_SIZE));
    testLinear(new PegasosSVM(DataUtils.DATA_SIZE));
  }

}
