package org.simpleml.classify.notify.progress;

/**
 * @author sitfoxfly
 */
public class AdvancedTrainingProgressPrinter implements TrainingProgressListener {

  private long iterationCounter = 0;
  private long iterationTime;
  private long trainingTime;

  @Override
  public void progressUpdate(TrainingProgressEvent event) {
    switch (event.getType()) {
      case START_TRAINING:
        iterationCounter = 0;
        trainingTime = System.currentTimeMillis();
        break;
      case START_ITERATION:
        iterationTime = System.currentTimeMillis();
        break;
      case FINISH_ITERATION:
        iterationCounter++;
        System.out.println("Iter #" + iterationCounter + " (" + (System.currentTimeMillis() - iterationTime) + "ms)");
        break;
      case FINISH_TRAINING:
        System.out.println("Finished... (" + (System.currentTimeMillis() - trainingTime) + "ms)");
        break;
    }
  }
}
