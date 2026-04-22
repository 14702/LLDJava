package com.ToDo.repository;

import com.ToDo.enums.ActivityType;
import com.ToDo.model.Activity;
import com.ToDo.model.Task;
import com.ToDo.exception.InvalidTaskIdException;
import com.ToDo.exception.TaskNotFoundException;
import com.ToDo.repository.interfaces.TaskRepository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class TaskRepositoryImpl implements TaskRepository {

    private final Map<String, Task> tasks = new ConcurrentHashMap<>();
    private final Map<String, List<String>> tagsToTaskIdMapping = new ConcurrentHashMap<>();
    private final List<Task> removedTasks = new CopyOnWriteArrayList<>();
    private final List<Activity> activityLog = new CopyOnWriteArrayList<>();

    @Override
    public void addTask(Task task) {
        if (tasks.containsKey(task.getTaskId())) {
            throw new InvalidTaskIdException("Task id already present");
        }
        task.setCreatedAt(Date.from(Instant.now()));
        tasks.put(task.getTaskId(), task);
        addTags(task);
        activityLog.add(new Activity(task.getTaskId(), task.getDetails(), ActivityType.ADDED));
    }

    @Override
    public Task getTask(String taskId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task not found: " + taskId);
        }
        return task;
    }

    @Override
    public void modifyTask(Task task) {
        if (!tasks.containsKey(task.getTaskId())) {
            throw new TaskNotFoundException("Task not found: " + task.getTaskId());
        }
        Task existing = tasks.get(task.getTaskId());
        removeTags(existing);
        task.setUpdatedAt(Date.from(Instant.now()));
        if (task.getCreatedAt() == null) {
            task.setCreatedAt(existing.getCreatedAt());
        }
        tasks.put(task.getTaskId(), task);
        addTags(task);
        activityLog.add(new Activity(task.getTaskId(), task.getDetails(), ActivityType.MODIFIED));
    }

    @Override
    public void removeTask(String taskId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task not found: " + taskId);
        }
        removeTags(task);
        removedTasks.add(task);
        tasks.remove(taskId);
        activityLog.add(new Activity(task.getTaskId(), task.getDetails(), ActivityType.REMOVED));
    }

    @Override
    public List<Task> listTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Map<String, List<String>> getTagsToTaskIdMapping() {
        return tagsToTaskIdMapping;
    }

    @Override
    public List<Task> getRemovedTasks() {
        return new ArrayList<>(removedTasks);
    }

    @Override
    public List<Activity> getActivityLog() {
        return new ArrayList<>(activityLog);
    }

    private void addTags(Task task) {
        if (task.getTags() == null) return;
        for (String tag : task.getTags()) {
            tagsToTaskIdMapping.computeIfAbsent(tag, k -> new CopyOnWriteArrayList<>()).add(task.getTaskId());
        }
    }

    private void removeTags(Task task) {
        if (task.getTags() == null) return;
        for (String tag : task.getTags()) {
            List<String> ids = tagsToTaskIdMapping.get(tag);
            if (ids != null) {
                ids.remove(task.getTaskId());
                if (ids.isEmpty()) {
                    tagsToTaskIdMapping.remove(tag);
                }
            }
        }
    }
}
