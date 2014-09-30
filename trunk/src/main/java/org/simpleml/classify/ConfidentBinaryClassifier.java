package org.simpleml.classify;

import org.simpleml.struct.Vector;

/**
 * @author rasmikun
 */
public interface ConfidentBinaryClassifier extends Classifier {

  public double classifyWithConfidence(Vector vector);

}
