package org.simpleml.classify;

import org.simpleml.struct.ArrayVector;
import org.simpleml.struct.LabeledVector;
import org.simpleml.struct.Vector;

/**
 * @author rasmikun
 */
public class PassiveAggressivePerceptron implements Classifier {

    private static final int DEFAULT_AGGRESSIVE_PARAMETER = 1;
    private static final int DEFAULT_NUM_ITERATION = 100;
    private static final setAlgorithmEnum DEFAULT_SET_ALGORITHM = setAlgorithmEnum.PA1;

    private int aggressive_parameter = DEFAULT_AGGRESSIVE_PARAMETER;

    private int numIteration = DEFAULT_NUM_ITERATION;

    private setAlgorithmEnum setType = DEFAULT_SET_ALGORITHM;

    private ArrayVector w;

    enum setAlgorithmEnum {PA1, PA2, PA3}

    private double setPA1(LabeledVector labeledVector, double l) {
        double vectorRate = labeledVector.getL2();
        return l / vectorRate * vectorRate;
    }

    private double setPA2(LabeledVector labeledVector, double l) {
        return Math.min(aggressive_parameter, setPA1(labeledVector, l));
    }

    private double setPA3(LabeledVector labeledVector, double l) {
        double vectorRate = labeledVector.getL2();
        return l / (vectorRate * vectorRate + 1 / (2 * aggressive_parameter));
    }

    public PassiveAggressivePerceptron(int dimension) {
        w = new ArrayVector(dimension);
    }

    @Override
    public void train(Iterable<LabeledVector> list) {
        for (int i = 0; i < numIteration; i++) {
            for (LabeledVector labeledVector : list) {
                double y = labeledVector.getLabel();
                // suffer loss
                double l = Math.max(0d, 1 - y * w.innerProduct(labeledVector));
                // set
                double tau = 0d;
                switch (setType) {
                    case PA1:
                        tau = setPA1(labeledVector, l);
                        break;
                    case PA2:
                        tau = setPA2(labeledVector, l);
                        break;
                    case PA3:
                        tau = setPA3(labeledVector, l);
                        break;
                }
                // update
                w.addToThis(labeledVector, tau * y);
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

    public void setAlgorithm(setAlgorithmEnum e) {
        setType = e;
    }
}
