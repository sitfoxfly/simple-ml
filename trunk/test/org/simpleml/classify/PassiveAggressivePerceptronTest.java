package org.simpleml.classify;

import junit.framework.Assert;
import org.junit.Test;
import org.simpleml.struct.ArrayVector;
import org.simpleml.struct.LabeledVector;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rasmi
 * Date: 7/9/12
 * Time: 12:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class PassiveAggressivePerceptronTest {
    @Test
    public void testPAPerceptron() throws Exception {
        PassiveAggressivePerceptron perceptron = new PassiveAggressivePerceptron(3);
        List<LabeledVector> trainingData = new LinkedList<LabeledVector>();

        trainingData.add(new LabeledVector(new ArrayVector(new double[]{0, -1, 0}), -1));
        trainingData.add(new LabeledVector(new ArrayVector(new double[]{1, 0, 1}), 1));
        trainingData.add(new LabeledVector(new ArrayVector(new double[]{0, 1, 0}), 1));
        trainingData.add(new LabeledVector(new ArrayVector(new double[]{1, 0, 1}), 1));
        trainingData.add(new LabeledVector(new ArrayVector(new double[]{-1, -1, -1}), -1));

        perceptron.train(trainingData);

        for (LabeledVector vector : trainingData) {
            int predictedLabel = perceptron.classify(vector.getInnerVector());
            Assert.assertEquals(vector.getLabel(), predictedLabel);
        }
    }
}
