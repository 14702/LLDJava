package com.ToDo.service;
import com.ToDo.model.Task;
import com.ToDo.model.TaskStatistics;
import java.util.Date;
import java.util.List;

public interface TodoService {

    void addTask(Task task);

    Task getTask(String taskId); //a task

    void modifyTask(Task task);

    void removeTask(String taskId);

    List<Task> listTasks(); // a list of tasks which match the given filter ordered based on a defined sort criteria

    List<Task> listTasks(Date endTime);

    List<Task> listTasks(Date startTime, Date endTime);

    List<Task> listTasks(List<String> tags);

    TaskStatistics getStatistics(); // statistics for the given time period (if supplied)

    TaskStatistics getStatistics(Date startTime, Date endTime); // statistics for the given time period (if supplied)

    void getActivityLog();// -> activity log for the given time period (if supplied)

    void getActivityLog(Date startTime, Date endTime);// -> activity log for the given time period (if supplied)
}
