package org.simpleml.classify;

import org.simpleml.struct.*;
import org.simpleml.util.VectorUtil;

import java.util.Iterator;

/**
 * See the paper: Shai Shalev-Shwartz at al., Pegasos: Primal Estimated sub-GrAdient SOlver for SVM
 *
 * @author sitfoxfly
 */
public class PegasosSVM implements Classifier {

    private static final int DEFAULT_NUM_ITERATIONS = 500;
    private static final double DEFAULT_REGULARIZATION = 1.0E-4;

    private int numIterations = DEFAULT_NUM_ITERATIONS;
    private double lambda = DEFAULT_REGULARIZATION;

    private MutableVector weights;

    private int dimension;
    private double alpha;
    private double mu;

    public PegasosSVM(int dimension) {
        this.dimension = dimension;
        weights = new ArrayVector(new double[dimension + 1]);
        alpha = 1.0;
        mu = 0.0;
    }

    public void train(Iterable<? extends Iterable<LabeledVector>> data) {
        int iteration = 2;
        while (iteration <= numIterations) {
            for (Iterable<LabeledVector> instances : data) {
                int k = 0;
                SparseHashVector updateVector = new SparseHashVector(weights.getDimension());
                for (LabeledVector instance : instances) {
                    BiasedVector biasedInstance = new BiasedVector(instance);
                    if (alpha * instance.getLabel() * weights.innerProduct(biasedInstance) < 1.0) {
                        updateVector.addToThis(biasedInstance, instance.getLabel());
                    }
                    k++;
                }

                final double nu = 1.0 / (lambda * iteration);
                final double coef1 = 1.0 - 1.0 / iteration;
                final double coef2 = coef1 * alpha;
                double newMu = mu * coef1 * coef1;
                double diff = 0;
                final Iterator<Vector.Entry> entryIterator = updateVector.sparseIterator();
                while (entryIterator.hasNext()) {
                    final Vector.Entry entry = entryIterator.next();
                    final int index = entry.getIndex();
                    final double x = entry.getValue();
                    final double z = weights.get(index);
                    diff += x * (2.0 * z * alpha * coef1 + nu * x / k);
                }
                newMu += nu * diff / k;
                weights.addToThis(updateVector, nu / (coef2 * k));
                double alpha12 = alpha * coef1;
                final double norm = Math.sqrt(lambda * newMu);
                if (norm < 1.0) {
                    mu = 1.0 / (newMu * lambda);
                    alpha = alpha12 / norm;
                } else {
                    mu = newMu;
                    alpha = alpha12;
                }

                iteration++;
                if (iteration > numIterations + 1) {
                    break;
                }
            }
        }
    }

    @Override
    public int classify(Vector vector) {
        return (int) Math.signum(weights.innerProduct(new BiasedVector(vector)));
    }

    public int getNumIterations() {
        return numIterations;
    }

    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public int getDimension() {
        return dimension;
    }

    public Vector getWeights() {
        return VectorUtil.immutableVector(weights);
    }

    /**
     * adds bias feature, what allow to learn bias term
     */
    private static class BiasedVector implements Vector {

        private int dimension;
        private Vector vector;

        @Override
        public double get(int index) {
            if (index == dimension - 1) {
                return 1.0;
            }
            return vector.get(index);
        }

        @Override
        public int getDimension() {
            return dimension;
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

        private BiasedVector(Vector vector) {
            this.vector = vector;
            this.dimension = vector.getDimension() + 1;
        }

    }
}
