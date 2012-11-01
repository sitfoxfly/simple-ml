package org.simpleml.classify.ext;

import junit.framework.Assert;
import org.junit.Test;
import org.simpleml.classify.*;
import org.simpleml.struct.LabeledVector;
import org.simpleml.struct.Vector;
import org.simpleml.util.VectorUtil;

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
        List<LabeledVector> trainingData = TestUtil.getSimpleTrainingData();
        trainable.train(trainingData);
        final ExternalizableModel loadedModel = ExtUtil.trySaveAndLoad((ExternalizableModel) trainable);

        final Vector v1 = (Vector) getWeights.invoke(trainable);
        final Vector v2 = (Vector) getWeights.invoke(loadedModel);
        Assert.assertTrue(VectorUtil.equals(v1, v2));
    }

    @Test
    public void testSaveAndLoadLinear() throws Exception {
        testLinear(new AveragedLinearPerceptron(TestUtil.DATA_SIZE));
        testLinear(new LinearPerceptron(TestUtil.DATA_SIZE));
        testLinear(new PassiveAggressivePerceptron(TestUtil.DATA_SIZE));
        testLinear(new PegasosSVM(TestUtil.DATA_SIZE));
    }

}
