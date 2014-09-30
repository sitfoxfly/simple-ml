package org.simpleml.classify.notify;

import org.simpleml.classify.notify.progress.TrainingProgressEvent;
import org.simpleml.classify.notify.progress.TrainingProgressListener;
import org.simpleml.classify.notify.progress.TrainingProgressNotifier;

import java.util.LinkedList;
import java.util.List;

/**
 * @author sitfoxfly
 */
public class Notifier implements TrainingProgressNotifier {

  private final List<TrainingProgressListener> trainingProgressListeners = new LinkedList<>();

  @Override
  public void addTrainingProgressListener(TrainingProgressListener listener) {
    trainingProgressListeners.add(listener);
  }

  @Override
  public void removeTrainingProgressListener(TrainingProgressListener listener) {
    trainingProgressListeners.remove(listener);
  }

  public void notifyTrainingProgressListeners(TrainingProgressEvent event) {
    for (TrainingProgressListener listener : trainingProgressListeners) {
      listener.progressUpdate(event);
    }
  }

}
