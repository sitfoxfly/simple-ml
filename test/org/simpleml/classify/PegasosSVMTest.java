package org.simpleml.classify;

import org.junit.Assert;
import org.junit.Test;
import org.simpleml.gen.EachInstanceGenerator;
import org.simpleml.struct.LabeledVector;

import java.util.List;

/**
 * @author sitfoxfly
 */
public class PegasosSVMTest {

    @Test
    public void testClassifier() throws Exception {
        PegasosSVM pegasosSVM = new PegasosSVM(3);
        List<LabeledVector> trainingData = TestUtil.getSimpleTrainingData();

        pegasosSVM.train(new EachInstanceGenerator<LabeledVector>(trainingData));

        for (LabeledVector vector : trainingData) {
            int predictedLabel = pegasosSVM.classify(vector.getInnerVector());
            Assert.assertEquals(vector.getLabel(), predictedLabel);
        }
    }
}