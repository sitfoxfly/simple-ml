package org.simpleml.classify;

import org.junit.Assert;
import org.junit.Test;
import org.simpleml.struct.LabeledVector;

import java.util.List;

/**
 * @author sitfoxfly
 */
public class PegasosSVMTest {

  @Test
  public void testClassifier() throws Exception {
    final PegasosSVM pegasosSVM = new PegasosSVM(DataUtils.DATA_SIZE);
    final List<LabeledVector> trainingData = DataUtils.getSimpleTrainingData();

    pegasosSVM.train(trainingData);

    for (LabeledVector vector : trainingData) {
      int predictedLabel = pegasosSVM.classify(vector.getInnerVector());
      Assert.assertEquals(vector.getLabel(), predictedLabel);
    }
  }
}
