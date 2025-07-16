package com.ToDo.model;
import java.util.List;

public class TaskStatistics {
    private List<Task> added;
    private List<Task> modified;
    private List<Task> removed;
    private List<Task> spilledOverDeadline;

    public TaskStatistics(List<Task> added, List<Task> modified, List<Task> removed, List<Task> spilledOverDeadline) {
        this.added = added;
        this.modified = modified;
        this.removed = removed;
        this.spilledOverDeadline = spilledOverDeadline;
    }

    public List<Task> getAdded() {
        return added;
    }

    public void setAdded(List<Task> added) {
        this.added = added;
    }

    public List<Task> getModified() {
        return modified;
    }

    public void setModified(List<Task> modified) {
        this.modified = modified;
    }

    public List<Task> getRemoved() {
        return removed;
    }

    public void setRemoved(List<Task> removed) {
        this.removed = removed;
    }

    public List<Task> getSpilledOverDeadline() {
        return spilledOverDeadline;
    }

    public void setSpilledOverDeadline(List<Task> spilledOverDeadline) {
        this.spilledOverDeadline = spilledOverDeadline;
    }
}
