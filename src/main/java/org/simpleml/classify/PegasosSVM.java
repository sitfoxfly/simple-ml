package org.simpleml.classify;

import org.simpleml.classify.ext.ExternalizableModel;
import org.simpleml.classify.ext.LoadException;
import org.simpleml.classify.notify.Notifier;
import org.simpleml.classify.notify.progress.TrainingProgressEvent;
import org.simpleml.classify.notify.progress.TrainingProgressListener;
import org.simpleml.classify.notify.progress.TrainingProgressNotifier;
import org.simpleml.struct.LabeledVector;
import org.simpleml.struct.MutableVector;
import org.simpleml.struct.Vector;
import org.simpleml.utils.VectorUtils;

import java.io.*;
import java.util.Iterator;

/**
 * See the paper: Shai Shalev-Shwartz at al., Pegasos: Primal Estimated sub-GrAdient SOlver for SVM
 *
 * @author sitfoxfly
 */
public class PegasosSVM implements ConfidentBinaryClassifier, Trainable, TrainingProgressNotifier, ExternalizableModel {

  public static PegasosSVM load(InputStream in) throws IOException, LoadException {
    final DataInputStream dataIn = new DataInputStream(in);
    final PegasosSVM instance = new PegasosSVM();
    instance.weights = (MutableVector) VectorUtils.loadDefaultVector(dataIn);
    instance.dimension = dataIn.readInt();
    instance.lambda = dataIn.readDouble();
    instance.alpha = dataIn.readDouble();
    instance.mu = dataIn.readDouble();
    return instance;
  }

  private static final int ITERATION_OFFSET = 2; // zero division error occurs when starts with 0 iteration

  private static final int DEFAULT_NUM_ITERATIONS = 500;
  private static final double DEFAULT_REGULARIZATION = 1.0E-4;

  private int numIteration = DEFAULT_NUM_ITERATIONS;
  private double lambda = DEFAULT_REGULARIZATION;

  private final Notifier notifier = new Notifier();

  private MutableVector weights;

  private int dimension;
  private double alpha;
  private double mu;

  private PegasosSVM() {
  }

  public PegasosSVM(int dimension) {
    this.dimension = dimension;
    weights = VectorUtils.newMutableVector(new double[dimension + 1]);
    alpha = 1.0;
    mu = 0.0;
  }

  @Override
  public void train(Iterable<LabeledVector> data) {
    notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.START_TRAINING));
    int localIteration = ITERATION_OFFSET;
    for (int iteration = 0; iteration < numIteration; iteration++) {
      notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.START_ITERATION));
      for (LabeledVector labeledVector : data) {
        notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.START_INSTANCE_PROCESSING));
        final MutableVector updateVector = VectorUtils.newSparseVector(weights.getDimension());
        final BiasedVector biasedVector = new BiasedVector(labeledVector.getInnerVector());
        if (alpha * labeledVector.getLabel() * weights.innerProduct(biasedVector) < 1.0) {
          updateVector.addToThis(biasedVector, labeledVector.getLabel());
        }

        final double nu = 1.0 / (lambda * localIteration);
        final double coef1 = 1.0 - 1.0 / localIteration;
        final double coef2 = coef1 * alpha;
        double newMu = mu * coef1 * coef1;
        double diff = 0;
        final Iterator<Vector.Entry> entryIterator = updateVector.sparseIterator();
        while (entryIterator.hasNext()) {
          final Vector.Entry entry = entryIterator.next();
          final int index = entry.getIndex();
          final double x = entry.getValue();
          final double z = weights.get(index);
          diff += x * (2.0 * z * coef2 + nu * x);
        }
        newMu += nu * diff;
        weights.addToThis(updateVector, nu / coef2);
        final double norm = Math.sqrt(lambda * newMu);
        if (norm > 1.0) {
          mu = 1.0 / lambda;
          alpha = coef2 / norm;
        } else {
          mu = newMu;
          alpha = coef2;
        }

        localIteration++;
        notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.FINISH_INSTANCE_PROCESSING));
      }
      notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.FINISH_ITERATION));
    }
    weights.scaleBy(alpha);
    alpha = 1.0;
    notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.FINISH_TRAINING));
  }

  public void trainBunch(Iterable<? extends Iterable<LabeledVector>> data) {
    notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.START_TRAINING));
    final int finalIteration = numIteration + ITERATION_OFFSET;
    int iteration = ITERATION_OFFSET;
    while (iteration < finalIteration) {
      notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.START_ITERATION));
      for (Iterable<LabeledVector> instances : data) {
        int k = 0;
        final MutableVector updateVector = VectorUtils.newSparseVector(weights.getDimension());
        for (LabeledVector instance : instances) {
          notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.START_INSTANCE_PROCESSING));
          BiasedVector biasedInstance = new BiasedVector(instance);
          if (alpha * instance.getLabel() * weights.innerProduct(biasedInstance) < 1.0) {
            updateVector.addToThis(biasedInstance, instance.getLabel());
          }
          k++;
          notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.FINISH_INSTANCE_PROCESSING));
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
          diff += x * (2.0 * z * coef2 + nu * x / k);
        }
        newMu += nu * diff / k;
        weights.addToThis(updateVector, nu / (coef2 * k));
        final double norm = Math.sqrt(lambda * newMu);
        if (norm > 1.0) {
          mu = 1.0 / lambda;
          alpha = coef2 / norm;
        } else {
          mu = newMu;
          alpha = coef2;
        }

        iteration++;
        notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.FINISH_ITERATION));
        if (iteration >= finalIteration) {
          break;
        }
      }
    }
    weights.scaleBy(alpha);
    alpha = 1.0;
    notifier.notifyTrainingProgressListeners(TrainingProgressEvent.event(TrainingProgressEvent.EventType.FINISH_TRAINING));
  }

  @Override
  public int classify(Vector vector) {
    return (int) Math.signum(weights.innerProduct(new BiasedVector(vector)));
  }

  @Override
  public double classifyWithConfidence(Vector vector) {
    return weights.innerProduct(new BiasedVector(vector));
  }

  public int getNumIteration() {
    return numIteration;
  }

  public void setNumIteration(int numIteration) {
    this.numIteration = numIteration;
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
    return VectorUtils.immutableVector(weights);
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
    VectorUtils.save(weights, dataOut);
    dataOut.writeInt(dimension);
    dataOut.writeDouble(lambda);
    dataOut.writeDouble(alpha);
    dataOut.writeDouble(mu);
    dataOut.flush();
  }

  /**
   * adds bias feature, what allow to learn bias term
   */
  private static class BiasedVector implements Vector {

    private int dimension;
    private Vector vector;

    private BiasedVector(Vector vector) {
      this.vector = vector;
      this.dimension = vector.getDimension() + 1;
    }

    @Override
    public double get(int index) {
      if (index == 0) {
        return 1.0;
      }
      return vector.get(index - 1);
    }

    @Override
    public int getDimension() {
      return dimension;
    }

    @Override
    public Iterator<Entry> sparseIterator() {
      return new Iterator<Entry>() {

        private Iterator<Entry> innerIterator = vector.sparseIterator();
        private boolean iterateBiasTerm = true;

        @Override
        public boolean hasNext() {
          return innerIterator.hasNext() || iterateBiasTerm;
        }

        @Override
        public Entry next() {
          if (iterateBiasTerm) {
            iterateBiasTerm = false;
            return new Entry() {
              @Override
              public int getIndex() {
                return dimension - 1;
              }

              @Override
              public double getValue() {
                return 1.0;
              }
            };
          } else if (innerIterator.hasNext()) {
            return new Entry() {

              private final Entry innerEntry = innerIterator.next();

              @Override
              public int getIndex() {
                return innerEntry.getIndex() + 1;
              }

              @Override
              public double getValue() {
                return innerEntry.getValue();
              }
            };
          }
          throw new IllegalStateException();
        }

        @Override
        public void remove() {
          throw new UnsupportedOperationException();
        }
      };
    }

    @Override
    public int sparseSize() {
      return vector.sparseSize();
    }

    @Override
    public double getL2() {
      throw new UnsupportedOperationException();
    }

    @Override
    public double innerProduct(Vector thatVector) {
      return vector.innerProduct(thatVector);
    }

  }

}
