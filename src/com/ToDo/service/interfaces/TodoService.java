package com.ToDo.service.interfaces;

import com.ToDo.model.Activity;
import com.ToDo.model.Task;
import com.ToDo.model.TaskStatistics;
import java.util.Date;
import java.util.List;

public interface TodoService {

    void addTask(Task task);

    Task getTask(String taskId);

    void modifyTask(Task task);

    void removeTask(String taskId);

    void completeTask(String taskId);

    List<Task> listTasks();

    List<Task> listTasks(Date endTime);

    List<Task> listTasks(Date startTime, Date endTime);

    List<Task> listTasks(List<String> tags);

    TaskStatistics getStatistics();

    TaskStatistics getStatistics(Date startTime, Date endTime);

    List<Activity> getActivityLog();

    List<Activity> getActivityLog(Date startTime, Date endTime);
}
