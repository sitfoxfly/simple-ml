package org.simpleml.classify.notify.progress;

/**
 * @author sitfoxfly
 */
public interface TrainingProgressNotifier {

    public void addTrainingProgressListener(TrainingProgressListener listener);

    public void removeTrainingProgressListener(TrainingProgressListener listener);

}
