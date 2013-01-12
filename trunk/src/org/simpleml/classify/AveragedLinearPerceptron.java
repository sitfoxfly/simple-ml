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
 * @author sitfoxfly
 */
public class AveragedLinearPerceptron implements ConfidentBinaryClassifier, Trainable, TrainingProgressNotifier, ExternalizableModel {

    public static AveragedLinearPerceptron load(InputStream in) throws IOException, LoadException {
        DataInputStream dataIn = new DataInputStream(in);
        AveragedLinearPerceptron instance = new AveragedLinearPerceptron();
        instance.w = (MutableVector) VectorUtil.load(ArrayVector.class, dataIn);
        instance.numIteration = dataIn.readInt();
        instance.learningRate = dataIn.readDouble();
        return instance;
    }

    private static final double DEFAULT_LEARNING_RATE = 1.0;
    private static final int DEFAULT_NUM_ITERATION = 100;

    private double learningRate = DEFAULT_LEARNING_RATE;
    private int numIteration = DEFAULT_NUM_ITERATION;

    private final Notifier notifier = new Notifier();

    private MutableVector w;

    private AveragedLinearPerceptron() {
    }

    public AveragedLinearPerceptron(int dimension) {
        this.w = new ArrayVector(dimension);
    }

    @Override
    public void train(Iterable<LabeledVector> data) {
        notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.START_TRAINING));
        int numSummed = 0;
        for (int iteration = 0; iteration < numIteration; iteration++) {
            notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.START_ITERATION));
            ArrayVector localWeights = new ArrayVector(w.getDimension());
            for (LabeledVector labeledVector : data) {
                notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.START_INSTANCE_PROCESSING));
                if (localWeights.innerProduct(labeledVector.getInnerVector()) * labeledVector.getLabel() <= 0) {
                    localWeights.addToThis(labeledVector.getInnerVector(), labeledVector.getLabel() * learningRate);
                }
                notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.FINISH_INSTANCE_PROCESSING));
            }
            w.addToThis(localWeights);
            numSummed++;
            notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.FINISH_ITERATION));
        }
        w.scaleBy(1.0 / numSummed);
        notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.FINISH_TRAINING));
        w = new SparseHashVector(w);
    }

    @Override
    public int classify(Vector vector) {
        return (int) Math.signum(w.innerProduct(vector));
    }

    @Override
    public double classifyWithConfidence(Vector vector) {
        return w.innerProduct(vector);
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
