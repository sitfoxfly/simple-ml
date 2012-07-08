package org.simpleml.classify;

import org.simpleml.struct.Vector;

/**
 * @author rasmikun
 */
public interface Classifier {

    public int classify(Vector vector);

}
