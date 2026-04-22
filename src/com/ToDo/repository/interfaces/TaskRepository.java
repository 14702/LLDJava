package com.ToDo.repository.interfaces;

import com.ToDo.model.Activity;
import com.ToDo.model.Task;
import java.util.*;

public interface TaskRepository {
    void addTask(Task task);

    Task getTask(String taskId);

    void modifyTask(Task task);

    void removeTask(String taskId);

    List<Task> listTasks();

    List<Task> getRemovedTasks();

    Map<String, List<String>> getTagsToTaskIdMapping();

    List<Activity> getActivityLog();
}
