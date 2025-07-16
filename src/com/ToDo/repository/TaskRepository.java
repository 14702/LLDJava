package com.ToDo.repository;
import com.ToDo.model.Task;
import java.util.*;

public interface TaskRepository {
    void addTask(Task task);

    Task getTask(String taskId); //a task

    void modifyTask(Task task);

    void removeTask(String taskId);

    List<Task> listTasks(); // a list of tasks which match the given filter ordered based on a defined sort criteria

    List<Task> getRemovedTasks();

    Map<String, List<String>> getTagsToTaskIdMapping();
}

