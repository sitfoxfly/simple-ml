package simpleml.classify;


import org.junit.Assert;
import org.junit.Test;
import simpleml.struct.LabeledVector;

import java.util.List;

/**
 * @author rasmikun
 */
public class PassiveAggressivePerceptronTest {

  @Test
  public void testPAPerceptron() throws Exception {
    final PassiveAggressivePerceptron perceptron = new PassiveAggressivePerceptron(DataUtils.DATA_SIZE);
    final List<LabeledVector> trainingData = DataUtils.getSimpleTrainingData();

    perceptron.train(trainingData);
    for (LabeledVector vector : trainingData) {
      int predictedLabel = perceptron.classify(vector.getInnerVector());
      Assert.assertEquals(vector.getLabel(), predictedLabel);
    }
  }
}
