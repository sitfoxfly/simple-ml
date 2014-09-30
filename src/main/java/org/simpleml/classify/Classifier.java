package org.simpleml.classify;

import org.simpleml.struct.Vector;

/**
 * @author sitfoxfly
 */
public interface Classifier {

  public int classify(Vector vector);
}
