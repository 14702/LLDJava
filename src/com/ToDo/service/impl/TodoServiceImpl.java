package com.ToDo.service.impl;

import com.ToDo.enums.ActivityType;
import com.ToDo.enums.TaskStatus;
import com.ToDo.exception.TaskNotFoundException;
import com.ToDo.model.Activity;
import com.ToDo.model.Task;
import com.ToDo.model.TaskStatistics;
import com.ToDo.repository.interfaces.TaskRepository;
import com.ToDo.service.interfaces.TodoService;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class TodoServiceImpl implements TodoService {

    private final TaskRepository taskRepository;

    public TodoServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void addTask(Task task) {
        taskRepository.addTask(task);
    }

    @Override
    public Task getTask(String taskId) {
        return taskRepository.getTask(taskId);
    }

    @Override
    public void modifyTask(Task task) {
        taskRepository.modifyTask(task);
    }

    @Override
    public void removeTask(String taskId) {
        taskRepository.removeTask(taskId);
    }

    @Override
    public void completeTask(String taskId) {
        Task task = taskRepository.getTask(taskId);
        task.setStatus(TaskStatus.COMPLETED);
        taskRepository.modifyTask(task);
        taskRepository.removeTask(taskId);
    }

    @Override
    public List<Task> listTasks() {
        return taskRepository.listTasks();
    }

    @Override
    public List<Task> listTasks(List<String> tags) {
        Map<String, List<String>> tagsToTaskIdMapping = taskRepository.getTagsToTaskIdMapping();
        Set<String> taskIds = new LinkedHashSet<>();
        for (String tag : tags) {
            List<String> ids = tagsToTaskIdMapping.get(tag);
            if (ids != null) {
                taskIds.addAll(ids);
            }
        }
        List<Task> tasks = new ArrayList<>();
        for (String id : taskIds) {
            try {
                tasks.add(getTask(id));
            } catch (TaskNotFoundException ignored) {
            }
        }
        return tasks;
    }

    @Override
    public List<Task> listTasks(Date endTime) {
        return taskRepository.listTasks().stream()
                .filter(t -> t.getDeadline() != null && t.getDeadline().before(endTime))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> listTasks(Date startTime, Date endTime) {
        return taskRepository.listTasks().stream()
                .filter(t -> t.getDeadline() != null
                        && !t.getDeadline().before(startTime)
                        && t.getDeadline().before(endTime))
                .collect(Collectors.toList());
    }

    @Override
    public TaskStatistics getStatistics() {
        List<Task> tasks = taskRepository.listTasks();
        Date now = Date.from(Instant.now());
        List<Task> spilledOver = tasks.stream()
                .filter(t -> t.getDeadline() != null && t.getDeadline().before(now))
                .collect(Collectors.toList());
        List<Task> modifiedTasks = tasks.stream()
                .filter(t -> t.getUpdatedAt() != null)
                .collect(Collectors.toList());
        List<Task> removedTasks = taskRepository.getRemovedTasks();
        return new TaskStatistics(tasks, modifiedTasks, removedTasks, spilledOver);
    }

    @Override
    public TaskStatistics getStatistics(Date startTime, Date endTime) {
        List<Activity> logs = taskRepository.getActivityLog().stream()
                .filter(a -> !a.getTimestamp().before(startTime) && a.getTimestamp().before(endTime))
                .collect(Collectors.toList());

        Set<String> addedIds = logs.stream().filter(a -> a.getType() == ActivityType.ADDED)
                .map(Activity::getTaskId).collect(Collectors.toSet());
        Set<String> modifiedIds = logs.stream().filter(a -> a.getType() == ActivityType.MODIFIED)
                .map(Activity::getTaskId).collect(Collectors.toSet());
        Set<String> removedIds = logs.stream().filter(a -> a.getType() == ActivityType.REMOVED)
                .map(Activity::getTaskId).collect(Collectors.toSet());

        List<Task> allTasks = taskRepository.listTasks();
        List<Task> removedTasks = taskRepository.getRemovedTasks();
        List<Task> allKnown = new ArrayList<>(allTasks);
        allKnown.addAll(removedTasks);
        Map<String, Task> taskMap = new HashMap<>();
        for (Task t : allKnown) {
            taskMap.put(t.getTaskId(), t);
        }

        List<Task> added = addedIds.stream().filter(taskMap::containsKey).map(taskMap::get).collect(Collectors.toList());
        List<Task> modified = modifiedIds.stream().filter(taskMap::containsKey).map(taskMap::get).collect(Collectors.toList());
        List<Task> removed = removedIds.stream().filter(taskMap::containsKey).map(taskMap::get).collect(Collectors.toList());

        Date now = Date.from(Instant.now()); //fff
        List<Task> spilledOver = allTasks.stream()
                .filter(t -> t.getDeadline() != null && t.getDeadline().before(now)
                        && t.getCreatedAt() != null
                        && !t.getCreatedAt().before(startTime) && t.getCreatedAt().before(endTime))
                .collect(Collectors.toList());

        return new TaskStatistics(added, modified, removed, spilledOver);
    }

    @Override
    public List<Activity> getActivityLog() {
        return taskRepository.getActivityLog();
    }

    @Override
    public List<Activity> getActivityLog(Date startTime, Date endTime) {
        return taskRepository.getActivityLog().stream()
                .filter(a -> !a.getTimestamp().before(startTime) && a.getTimestamp().before(endTime))
                .collect(Collectors.toList());
    }
}
