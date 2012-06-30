package org.simpleml.classify;

import org.simpleml.struct.ArrayVector;
import org.simpleml.struct.LabeledVector;
import org.simpleml.struct.MutableVector;
import org.simpleml.struct.Vector;
import org.simpleml.util.VectorUtil;

/**
 * @author sitfoxfly
 */
public class AveragedLinearPerceptron implements Classifier {

    private static final double DEFAULT_LEARNING_RATE = 1.0;
    private static final int DEFAULT_NUM_ITERATION = 1;

    private double learningRate = DEFAULT_LEARNING_RATE;
    private int numIteration = DEFAULT_NUM_ITERATION;

    private MutableVector w;

    public AveragedLinearPerceptron(int dimension) {
        this.w = new ArrayVector(dimension);
    }

    @Override
    public void train(Iterable<LabeledVector> list) {
        int numSummed = 0;
        for (int iteration = 0; iteration < numIteration; iteration++) {
            ArrayVector localWeights = new ArrayVector(w.getDimension());
            for (LabeledVector labeledVector : list) {
                if (localWeights.innerProduct(labeledVector.getInnerVector()) * labeledVector.getLabel() <= 0) {
                    localWeights.addToThis(labeledVector.getInnerVector(), labeledVector.getLabel() * learningRate);
                }
            }
            w.addToThis(localWeights);
            numSummed++;
        }
        w.product(1.0 / numSummed);
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
