package org.simpleml.classify;

import org.simpleml.struct.ArrayVector;
import org.simpleml.struct.LabeledVector;
import org.simpleml.struct.Vector;

/**
 * @author rasmikun
 */
public class PassiveAggressivePerceptron implements Classifier {

    enum algorithmType {PA1, PA2, PA3}

    private static final int DEFAULT_AGGRESSIVE_PARAMETER = 1;
    private static final int DEFAULT_NUM_ITERATION = 100;
    private static final algorithmType DEFAULT_SET_ALGORITHM = algorithmType.PA1;

    private int aggressive_parameter = DEFAULT_AGGRESSIVE_PARAMETER;
    private int numIteration = DEFAULT_NUM_ITERATION;
    private algorithmType setType = DEFAULT_SET_ALGORITHM;

    private ArrayVector w;

    private double algorithmPA1(LabeledVector labeledVector, double l) {
        double vectorRate = labeledVector.getL2();
        return l / vectorRate * vectorRate;
    }

    private double algorithmPA2(LabeledVector labeledVector, double l) {
        return Math.min(aggressive_parameter, algorithmPA1(labeledVector, l));
    }

    private double algorithmPA3(LabeledVector labeledVector, double l) {
        double vectorRate = labeledVector.getL2();
        return l / (vectorRate * vectorRate + 1 / (2 * aggressive_parameter));
    }

    public PassiveAggressivePerceptron(int dimension) {
        w = new ArrayVector(dimension);
    }

    public void train(Iterable<LabeledVector> list) {
        for (int i = 0; i < numIteration; i++) {
            for (LabeledVector labeledVector : list) {
                double sufferLoss = Math.max(0d, 1 - labeledVector.getLabel() * w.innerProduct(labeledVector));
                double updateValue = 0d;
                switch (setType) {
                    case PA1:
                        updateValue = algorithmPA1(labeledVector, sufferLoss);
                        break;
                    case PA2:
                        updateValue = algorithmPA2(labeledVector, sufferLoss);
                        break;
                    case PA3:
                        updateValue = algorithmPA3(labeledVector, sufferLoss);
                        break;
                }
                w.addToThis(labeledVector, updateValue * labeledVector.getLabel());
            }
        }
    }

    @Override
    public int classify(Vector vector) {
        return (int) Math.signum(w.innerProduct(vector));
    }

    public int getAggressive_parameter() {
        return aggressive_parameter;
    }

    public void setAggressive_parameter(int aggressive_parameter) {
        this.aggressive_parameter = aggressive_parameter;
    }

    public void setNumIteration(int numIteration) {
        this.numIteration = numIteration;
    }

    public int getNumIteration() {
        return numIteration;
    }

    public void setAlgorithm(algorithmType e) {
        setType = e;
    }
}
