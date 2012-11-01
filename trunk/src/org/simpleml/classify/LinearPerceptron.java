package org.simpleml.classify;

import org.simpleml.classify.ext.ExternalizableModel;
import org.simpleml.classify.ext.LoadException;
import org.simpleml.classify.notify.Notifier;
import org.simpleml.classify.notify.progress.TrainingProgressEvent;
import org.simpleml.classify.notify.progress.TrainingProgressListener;
import org.simpleml.classify.notify.progress.TrainingProgressNotifier;
import org.simpleml.struct.*;
import org.simpleml.util.VectorUtil;

import java.io.*;

/**
 * @author rasmikun
 */
public class LinearPerceptron implements Classifier, Trainable, TrainingProgressNotifier, ExternalizableModel {

    public static LinearPerceptron load(InputStream in) throws IOException, LoadException {
        DataInputStream dataIn = new DataInputStream(in);
        LinearPerceptron instance = new LinearPerceptron();
        instance.w = (MutableVector) VectorUtil.load(ArrayVector.class, dataIn);
        instance.numIteration = dataIn.readInt();
        instance.learningRate = dataIn.readDouble();
        return instance;
    }

    private static final double DEFAULT_LEARNING_RATE = 1.0;
    private static final int DEFAULT_NUM_ITERATION = 100;

    private double learningRate = DEFAULT_LEARNING_RATE;
    private int numIteration = DEFAULT_NUM_ITERATION;

    private Notifier notifier = new Notifier();

    private MutableVector w;

    private LinearPerceptron() {
    }

    public LinearPerceptron(int dimension) {
        this.w = new ArrayVector(dimension);
    }

    @Override
    public void train(Iterable<LabeledVector> data) {
        notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.START_TRAINING));
        for (int iteration = 0; iteration < numIteration; iteration++) {
            notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.START_ITERATION));
            for (LabeledVector labeledVector : data) {
                notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.START_INSTANCE_PROCESSING));
                if (w.innerProduct(labeledVector.getInnerVector()) * labeledVector.getLabel() <= 0) {
                    w.addToThis(labeledVector.getInnerVector(), labeledVector.getLabel() * learningRate);
                }
                notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.FINISH_INSTANCE_PROCESSING));
            }
            notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.FINISH_ITERATION));
        }
        notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.FINISH_TRAINING));

        w = new SparseHashVector(this.w);
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

    @Override
    public void addTrainingProgressListener(TrainingProgressListener listener) {
        notifier.addTrainingProgressListener(listener);
    }

    @Override
    public void removeTrainingProgressListener(TrainingProgressListener listener) {
        notifier.removeTrainingProgressListener(listener);
    }

    @Override
    public void save(OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        VectorUtil.save(w, dataOut);
        dataOut.writeInt(numIteration);
        dataOut.writeDouble(learningRate);
        dataOut.flush();
    }

}
