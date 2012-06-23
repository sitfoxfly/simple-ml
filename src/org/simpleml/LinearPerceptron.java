package org.simpleml;

/**
 * @author rasmikun
 */
public class LinearPerceptron implements Classifier {

    private static final double DEFAULT_LEARNING_RATE = 1.0;
    private static final int DEFAULT_NUM_ITERATION = 1000;

    private double learningRate = DEFAULT_LEARNING_RATE;
    private int numIteration = DEFAULT_NUM_ITERATION;

    private Vector w;

    public LinearPerceptron(Vector w) {
        this.w = w;
    }

    @Override
    public void train(Iterable<LabeledVector> list) {
        for (int iteration = 0; iteration < numIteration; iteration++) {
            for (LabeledVector labeledVector : list) {
                if (w.innerProduct(labeledVector) * labeledVector.getLabel() <= 0) {
                    w.addToThis(labeledVector, labeledVector.getLabel() * learningRate);
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
        return w;
    }
}
