package org.simpleml.classify.notify.progress;

import java.util.EnumMap;

/**
 * @author sitfoxfly
 */
public class TrainingProgressEvent {

    private static final EnumMap<EventType, TrainingProgressEvent> TYPE_TO_EVENT = new EnumMap<EventType, TrainingProgressEvent>(EventType.class);

    static {
        for (EventType type : EventType.values()) {
            TYPE_TO_EVENT.put(type, new TrainingProgressEvent(type));
        }
    }

    public static TrainingProgressEvent event(EventType type) {
        return TYPE_TO_EVENT.get(type);
    }

    public static enum EventType {
        START_TRAINING,
        START_ITERATION,
        START_INSTANCE_PROCESSING,
        FINISH_INSTANCE_PROCESSING,
        FINISH_ITERATION,
        FINISH_TRAINING,
    }

    private EventType type;

    public TrainingProgressEvent(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }

}
