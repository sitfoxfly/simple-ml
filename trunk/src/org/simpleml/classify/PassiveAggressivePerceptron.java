package org.simpleml.classify;

import org.simpleml.struct.ArrayVector;
import org.simpleml.struct.LabeledVector;
import org.simpleml.struct.MutableVector;
import org.simpleml.struct.Vector;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * See the paper: Koby Crammer at al. 2006. Online Passive-Aggressive Algorithms.
 *
 * @author rasmikun
 * @author sitfoxfly
 */
public class PassiveAggressivePerceptron implements Classifier {

    public static enum AlgorithmType {
        PA1,
        PA2,
        PA3
    }

    private static final AlgorithmType DEFAULT_ALGORITHM = AlgorithmType.PA2;
    private static final double DEFAULT_AGGRESSIVENESS = 0.5;
    private static final int DEFAULT_NUM_ITERATION = 100;

    private double aggressiveness = DEFAULT_AGGRESSIVENESS;
    private int numIteration = DEFAULT_NUM_ITERATION;
    private AlgorithmType algorithm = DEFAULT_ALGORITHM;

    private MutableVector w;

    public PassiveAggressivePerceptron(int dimension) {
        w = new ArrayVector(dimension);
    }

    private double calcSquaredL2(Vector vector) {
        final double l2 = vector.getL2();
        return l2 * l2;
    }

    private double getLR1(Vector vector, double lossValue) {
        return lossValue / calcSquaredL2(vector);
    }

    private double getLR2(Vector vector, double lossValue) {
        return Math.min(aggressiveness, getLR1(vector, lossValue));
    }

    private double getLR3(Vector vector, double lossValue) {
        double squaredL2 = calcSquaredL2(vector);
        return lossValue / (squaredL2 + 0.5 / aggressiveness);
    }

    public void train(Iterable<LabeledVector> data) {
        for (int i = 0; i < numIteration; i++) {
            for (LabeledVector vector : data) {
                final Vector innerVector = vector.getInnerVector();
                double lossValue = Math.max(0d, 1 - vector.getLabel() * w.innerProduct(innerVector));
                double learningRate = 0d;
                switch (algorithm) {
                    case PA1:
                        learningRate = getLR1(innerVector, lossValue);
                        break;
                    case PA2:
                        learningRate = getLR2(innerVector, lossValue);
                        break;
                    case PA3:
                        learningRate = getLR3(innerVector, lossValue);
                        break;
                }
                w.addToThis(innerVector, learningRate * vector.getLabel());
            }
        }
    }

    public void train(Iterable<LabeledVector> data, boolean cacheL2) {
        if (cacheL2) {
            LinkedList<LabeledVector> list = new LinkedList<LabeledVector>();
            for (LabeledVector vector : data) {
                list.add(new LabeledVector(new VectorWithCachedL2(vector.getInnerVector()), vector.getLabel()));
            }
            train(list);
        } else {
            train(data);
        }
    }

    @Override
    public int classify(Vector vector) {
        return (int) Math.signum(w.innerProduct(vector));
    }

    public double getAggressiveness() {
        return aggressiveness;
    }

    public void setAggressiveness(double aggressiveness) {
        this.aggressiveness = aggressiveness;
    }

    public void setNumIteration(int numIteration) {
        this.numIteration = numIteration;
    }

    public int getNumIteration() {
        return numIteration;
    }

    public void setAlgorithm(AlgorithmType algorithm) {
        this.algorithm = algorithm;
    }

    public AlgorithmType getAlgorithm() {
        return algorithm;
    }

    private static class VectorWithCachedL2 implements Vector {

        private Vector vector;

        private double cachedL2;
        private boolean isCached;

        private VectorWithCachedL2(Vector vector) {
            this.vector = vector;
        }

        @Override
        public double get(int index) {
            return vector.get(index);
        }

        @Override
        public int getDimension() {
            return vector.getDimension();
        }

        @Override
        public Iterator<Entry> sparseIterator() {
            return vector.sparseIterator();
        }

        @Override
        public int sparseSize() {
            return vector.sparseSize();
        }

        @Override
        public double innerProduct(Vector thatVector) {
            return vector.innerProduct(thatVector);
        }

        @Override
        public double getL2() {
            if (!isCached) {
                cachedL2 = vector.getL2();
                isCached = true;
            }
            return cachedL2;
        }

    }
}
