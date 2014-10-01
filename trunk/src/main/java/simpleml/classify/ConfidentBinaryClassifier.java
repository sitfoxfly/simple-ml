package simpleml.classify;

import simpleml.struct.Vector;

/**
 * @author rasmikun
 */
public interface ConfidentBinaryClassifier extends Classifier {

  public double classifyWithConfidence(Vector vector);

}
