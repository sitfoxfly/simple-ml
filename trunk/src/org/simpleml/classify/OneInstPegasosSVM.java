package org.simpleml.classify;

import org.simpleml.struct.*;
import org.simpleml.util.VectorUtil;

import java.util.Arrays;
import java.util.Iterator;

/**
 * See the paper: Shai Shalev-Shwartz at al., Pegasos: Primal Estimated sub-GrAdient SOlver for SVM
 *
 * @author sitfoxfly
 */
public class OneInstPegasosSVM implements Classifier {

    private static final int DEFAULT_NUM_ITERATIONS = 100;
    private static final double DEFAULT_REGULARIZATION = 1.0;

    private int numIterations = DEFAULT_NUM_ITERATIONS;
    private double lambda = DEFAULT_REGULARIZATION;

    private LabeledVector zeroVector;

    private int dimension;
    private MutableVector weights;
    private double alpha;
    private double mu;

    public OneInstPegasosSVM(int dimension) {
        this.dimension = dimension;
        zeroVector = new LabeledVector(VectorUtil.immutableVector(new SparseHashVector(dimension)), 0);
        double[] initWeights = new double[dimension];
        Arrays.fill(initWeights, 1.0);
        weights = new ArrayVector(initWeights);
        alpha = 1.0;
        mu = 1.0;
    }

    private double sqr(double value) {
        return value * value;
    }

    @Override
    public void train(Iterable<LabeledVector> list) {
        mu = 1.0 / lambda;
        alpha = Math.sqrt(mu / dimension);
        int iteration = 1;

        LabeledVector updateVector;

        while (iteration <= numIterations) {
            for (LabeledVector instance : list) {
                if (alpha * instance.getLabel() * weights.innerProduct(instance.getInnerVector()) < 1.0) {
                    updateVector = instance;
                } else {
                    updateVector = zeroVector;
                }
                final double nuT = 1.0 / (lambda * iteration);
                final Iterator<Vector.Entry> entryIterator = updateVector.sparseIterator();
                final double coef1 = 1.0 - nuT * lambda;
                final double coef2 = sqr(coef1);
                double newMu = coef2 * mu;
                while (entryIterator.hasNext()) {
                    final Vector.Entry entry = entryIterator.next();
                    final int index = entry.getIndex();
                    final double x = entry.getValue();
                    final double z = weights.get(index);
                    newMu = newMu - coef2 * sqr(alpha * z) + sqr(coef1 * z + nuT * updateVector.getLabel() * x);
                }
                weights.addToThis(updateVector.getInnerVector(), nuT * updateVector.getLabel() / (coef1 * alpha));
                alpha *= coef1;
                alpha = Math.min(alpha, alpha / (Math.sqrt(lambda * newMu)));
                mu = newMu * alpha * alpha;

                iteration++;
                if (iteration > numIterations) {
                    break;
                }
            }
        }
    }

    @Override
    public int classify(Vector vector) {
        return (int) Math.signum(weights.innerProduct(vector));
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
}
