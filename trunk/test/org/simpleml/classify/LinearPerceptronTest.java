package org.simpleml.classify;

import junit.framework.Assert;
import org.junit.Test;
import org.simpleml.struct.LabeledVector;

import java.util.List;

/**
 * @author rasmikun
 */
public class LinearPerceptronTest {

    @Test
    public void testLinearPerceptron() throws Exception {
        LinearPerceptron perceptron = new LinearPerceptron(TestUtil.DATA_SIZE);
        List<LabeledVector> trainingData = TestUtil.getSimpleTrainingData();

        perceptron.train(trainingData);

        for (LabeledVector vector : trainingData) {
            int predictedLabel = perceptron.classify(vector.getInnerVector());
            Assert.assertEquals(vector.getLabel(), predictedLabel);
        }
    }

}
