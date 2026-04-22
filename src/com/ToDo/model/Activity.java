package com.ToDo.model;

import com.ToDo.enums.ActivityType;
import java.time.Instant;
import java.util.Date;

public class Activity {
    private final String taskId;
    private final String taskDetails;
    private final ActivityType type;
    private final Date timestamp;

    public Activity(String taskId, String taskDetails, ActivityType type) {
        this.taskId = taskId;
        this.taskDetails = taskDetails;
        this.type = type;
        this.timestamp = Date.from(Instant.now());
    }

    public String getTaskId() { return taskId; }
    public String getTaskDetails() { return taskDetails; }
    public ActivityType getType() { return type; }
    public Date getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "[" + type + "] " + taskDetails + " (id=" + taskId + ") at " + timestamp;
    }
}
