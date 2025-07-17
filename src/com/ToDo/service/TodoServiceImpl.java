package com.ToDo.service;

import com.ToDo.exception.TaskNotFoundException;
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
        /*if (task.getStatus().equals(TaskStatus.COMPLETED)) {
            removeTask(task.getTaskId());
            return;
        }*/
        taskRepository.modifyTask(task);
    }

    @Override
    public void removeTask(String taskId) {
        taskRepository.removeTask(taskId);
    }

    @Override
    public List<Task> listTasks() {
        return taskRepository.listTasks();
    }

    public List<Task> listTasks(List<String> tags) {
        Map<String, List<String>> tagsToTaskIdMapping = taskRepository.getTagsToTaskIdMapping();
        Set<String> taskIds = new HashSet<>();
        for (String tag : tags) {
            if (!tagsToTaskIdMapping.containsKey(tag)) {
                continue;
            }
            taskIds.addAll(tagsToTaskIdMapping.get(tag));
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
        List<Task> tasks = taskRepository.listTasks();
        return tasks.parallelStream()
                .filter(e -> e.getDeadline().before(endTime))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> listTasks(Date startTime, Date endTime) {
        List<Task> tasks = taskRepository.listTasks();
        return tasks.parallelStream()
                .filter(e -> e.getCreatedAt().after(startTime) && e.getDeadline().before(endTime))
                .collect(Collectors.toList());
    }

    @Override
    public TaskStatistics getStatistics() {
        List<Task> tasks = taskRepository.listTasks();
        List<Task> spilledOver = tasks.stream().filter(e -> e.getDeadline().before(Date.from(Instant.now()))).collect(Collectors.toList());
        List<Task> modifiedTasks = tasks.stream().filter(e -> e.getUpdatedAt() != null).collect(Collectors.toList());
        List<Task> removedTasks = taskRepository.getRemovedTasks();
        return new TaskStatistics(tasks, modifiedTasks, removedTasks, spilledOver);
    }

    @Override
    public TaskStatistics getStatistics(Date startTime, Date endTime) {
        return null;
    }

    @Override
    public void getActivityLog() {

    }

    @Override
    public void getActivityLog(Date startTime, Date endTime) {

    }
}
