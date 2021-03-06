package simpleml.classify;

import simpleml.classify.ext.ExternalizableModel;
import simpleml.classify.ext.LoadException;
import simpleml.classify.notify.Notifier;
import simpleml.classify.notify.progress.TrainingProgressEvent;
import simpleml.classify.notify.progress.TrainingProgressListener;
import simpleml.classify.notify.progress.TrainingProgressNotifier;
import simpleml.struct.LabeledVector;
import simpleml.struct.MutableVector;
import simpleml.struct.Vector;
import simpleml.utils.VectorUtils;

import java.io.*;

/**
 * @author rasmikun
 */
public class LinearPerceptron implements ConfidentBinaryClassifier, Trainable, TrainingProgressNotifier, ExternalizableModel {

  public static LinearPerceptron load(InputStream in) throws IOException, LoadException {
    final DataInputStream dataIn = new DataInputStream(in);
    final LinearPerceptron instance = new LinearPerceptron();
    instance.w = (MutableVector) VectorUtils.loadDefaultVector(dataIn);
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

  private LinearPerceptron() {
  }

  public LinearPerceptron(int dimension) {
    this.w = VectorUtils.newMutableVector(dimension);
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
    return VectorUtils.immutableVector(w);
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
    final DataOutputStream dataOut = new DataOutputStream(out);
    VectorUtils.save(w, dataOut);
    dataOut.writeInt(numIteration);
    dataOut.writeDouble(learningRate);
    dataOut.flush();
  }

}
