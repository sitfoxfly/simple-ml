package simpleml.classify;

import simpleml.struct.LabeledVector;

/**
 * @author sitfoxfly
 */
public interface Trainable {

  public void train(Iterable<LabeledVector> data);

}
