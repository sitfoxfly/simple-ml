package org.simpleml.classify;

import org.simpleml.struct.LabeledVector;
import org.simpleml.struct.Vector;

/**
 * @author rasmikun
 */
public interface Classifier {

    public void train(Iterable<LabeledVector> list);

    public int classify(Vector l);

}
