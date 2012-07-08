package org.simpleml.classify;

import org.junit.Assert;
import org.junit.Test;
import org.simpleml.gen.EachInstanceGenerator;
import org.simpleml.struct.ArrayVector;
import org.simpleml.struct.LabeledVector;

import java.util.LinkedList;
import java.util.List;

/**
 * @author sitfoxfly
 */
public class PegasosSVMTest {

    @Test
    public void testClassifier() throws Exception {
        PegasosSVM pegasosSVM = new PegasosSVM(3);
        List<LabeledVector> trainingData = new LinkedList<LabeledVector>();

        trainingData.add(new LabeledVector(new ArrayVector(new double[]{1, 0, 1}), 1));
        trainingData.add(new LabeledVector(new ArrayVector(new double[]{0, 1, 0}), 1));
        trainingData.add(new LabeledVector(new ArrayVector(new double[]{1, 0, 1}), 1));
        trainingData.add(new LabeledVector(new ArrayVector(new double[]{-1, -1, -1}), -1));
        trainingData.add(new LabeledVector(new ArrayVector(new double[]{0, -1, 0}), -1));

        pegasosSVM.train(new EachInstanceGenerator<LabeledVector>(trainingData));

        for (LabeledVector vector : trainingData) {
            int predictedLabel = pegasosSVM.classify(vector.getInnerVector());
            System.out.println("expected = " + vector.getLabel() + "; predicted = " + predictedLabel + ";");
            Assert.assertEquals(vector.getLabel(), predictedLabel);
        }
    }
}
