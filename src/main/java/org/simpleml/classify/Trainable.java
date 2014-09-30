package org.simpleml.classify;

import org.simpleml.struct.LabeledVector;

/**
 * @author sitfoxfly
 */
public interface Trainable {

  public void train(Iterable<LabeledVector> data);

}
