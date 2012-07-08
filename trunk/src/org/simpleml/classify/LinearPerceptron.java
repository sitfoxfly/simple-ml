package org.simpleml.classify;

import org.simpleml.struct.ArrayVector;
import org.simpleml.struct.LabeledVector;
import org.simpleml.struct.MutableVector;
import org.simpleml.struct.Vector;
import org.simpleml.util.VectorUtil;

/**
 * @author rasmikun
 */
public class LinearPerceptron implements Classifier {

    private static final double DEFAULT_LEARNING_RATE = 1.0;
    private static final int DEFAULT_NUM_ITERATION = 100;

    private double learningRate = DEFAULT_LEARNING_RATE;
    private int numIteration = DEFAULT_NUM_ITERATION;

    private MutableVector w;

    public LinearPerceptron(int dimension) {
        this.w = new ArrayVector(dimension);
    }

    public void train(Iterable<LabeledVector> data) {
        for (int iteration = 0; iteration < numIteration; iteration++) {
            for (LabeledVector labeledVector : data) {
                if (w.innerProduct(labeledVector.getInnerVector()) * labeledVector.getLabel() <= 0) {
                    w.addToThis(labeledVector.getInnerVector(), labeledVector.getLabel() * learningRate);
                }
            }
        }
    }

    @Override
    public int classify(Vector vector) {
        return (int) Math.signum(w.innerProduct(vector));
    }

    public int getNumIteration() {
        return numIteration;
    }

    public void setNumIteration(int numIteration) {
        this.numIteration = numIteration;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public Vector getWeights() {
        return VectorUtil.immutableVector(w);
    }

}
