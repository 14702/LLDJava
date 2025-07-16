package com.ToDo.repository;
import com.ToDo.model.Task;
import com.ToDo.exception.InvalidTaskIdException;
import com.ToDo.exception.TaskNotFoundException;
import java.time.Instant;
import java.util.*;

public class TaskRepositoryImpl implements TaskRepository {

    private final Map<String, Task> tasks;
    private final Map<String, List<String>> tagsToTaskIdMapping;
    private List<Task> removedTasks;

    public TaskRepositoryImpl() {
        this.tasks = new HashMap<>();
        this.tagsToTaskIdMapping = new HashMap<>();
    }

    @Override
    public void addTask(Task task) {
        if (tasks.containsKey(task.getTaskId())) {
            throw new InvalidTaskIdException("Task id already present");
        }
        task.setCreatedAt(Date.from(Instant.now()));
        tasks.put(task.getTaskId(), task);
        addTags(task);
    }

    @Override
    public Task getTask(String taskId) {
        if (tasks.containsKey(taskId)) {
            return tasks.get(taskId);
        }
        throw new TaskNotFoundException();
    }

    @Override
    public void modifyTask(Task task) {
        if (!tasks.containsKey(task.getTaskId())) {
            throw new TaskNotFoundException();
        }
        task.setUpdatedAt(Date.from(Instant.now()));
        tasks.put(task.getTaskId(), task); // Assuming tags cannot be modified.
    }

    @Override
    public void removeTask(String taskId) {
        if (!tasks.containsKey(taskId)) {
            throw new TaskNotFoundException();
        }
        removedTasks.add(getTask(taskId));
        tasks.remove(taskId);
    }

    @Override
    public List<Task> listTasks() {
        if (tasks.isEmpty()) {
            return new ArrayList<>();
        }
        return tasks.values().stream().toList();
    }

    @Override
    public Map<String, List<String>> getTagsToTaskIdMapping() {
        return tagsToTaskIdMapping;
    }

    public List<Task> getRemovedTasks() {
        return removedTasks;
    }

    private void addTags(Task task) {
        if (task.getTags() != null) {
            task.getTags().forEach(e -> addTags(e, task));
        }
    }

    private void addTags(String tag, Task task) {
        tagsToTaskIdMapping.computeIfAbsent(tag, k -> new ArrayList<>()).add(task.getTaskId());
        tagsToTaskIdMapping.computeIfPresent(tag, (k, v) -> {
            v.add(task.getTaskId());
            return v;
        });
    }
}
