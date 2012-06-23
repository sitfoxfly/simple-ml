package org.simpleml;

/**
 * @author rasmikun
 */
public interface Classifier {

    public void train(Iterable<LabeledVector> list);

    public int classify(Vector l);

}
