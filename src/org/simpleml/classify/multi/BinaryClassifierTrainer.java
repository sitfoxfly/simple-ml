package org.simpleml.classify.multi;

import org.simpleml.classify.ConfidentBinaryClassifier;
import org.simpleml.struct.LabeledVector;

import java.util.List;

/**
 * @author sitfoxfly
 */
public interface BinaryClassifierTrainer {

    public ConfidentBinaryClassifier train(List<LabeledVector> data1, List<LabeledVector> data2);

}
