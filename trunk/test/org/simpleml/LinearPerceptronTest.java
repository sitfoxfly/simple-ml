package org.simpleml;

import junit.framework.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * @author rasmikun
 */
public class LinearPerceptronTest {

    @Test
    public void testLinearPerceptron() throws Exception {
        Vector w = new ArrayVector(new double[3]);
        LinearPerceptron perceptron = new LinearPerceptron(w);
        List<LabeledVector> trainingData = new LinkedList<LabeledVector>();

        trainingData.add(new LabeledVector(new ArrayVector(new double[]{1, 0, 1}), 1));
        trainingData.add(new LabeledVector(new ArrayVector(new double[]{0, 1, 0}), 1));
        trainingData.add(new LabeledVector(new ArrayVector(new double[]{1, 0, 1}), 1));

        trainingData.add(new LabeledVector(new ArrayVector(new double[]{-1, -1, -1}), -1));
        trainingData.add(new LabeledVector(new ArrayVector(new double[]{0, -1, 0}), -1));

        perceptron.train(trainingData);

        for (LabeledVector vector : trainingData) {
            int predictedLabel = perceptron.classify(vector);
            Assert.assertEquals(vector.getLabel(), predictedLabel);
        }
    }

}
