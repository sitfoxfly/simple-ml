package simpleml.classify.multi;

import simpleml.classify.ConfidentBinaryClassifier;
import simpleml.struct.LabeledVector;

import java.util.List;

/**
 * @author sitfoxfly
 */
public interface BinaryClassifierTrainer {

  public ConfidentBinaryClassifier train(List<LabeledVector> data1, List<LabeledVector> data2);

}
