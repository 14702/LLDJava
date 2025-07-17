package com.ToDo;

import com.ToDo.repository.interfaces.TaskRepository;
import com.ToDo.repository.TaskRepositoryImpl;
import com.ToDo.service.interfaces.TodoService;
import com.ToDo.service.TodoServiceImpl;
import com.ToDo.model.Task;
import com.ToDo.enums.TaskStatus;
import com.ToDo.model.TaskStatistics;

import java.util.*;
import java.text.SimpleDateFormat;

public class ToDoApplication {
    public static void main(String[] args) {
        // Setup
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        TaskRepository taskRepository = new TaskRepositoryImpl();
        TodoService todoService = new TodoServiceImpl(taskRepository);

        System.out.println("---------------------------------------------------------------------------------------------------");
        System.out.println("ADDING TASKS");
        Task t1 = new Task("Buy groceries", new Date(System.currentTimeMillis() + 24 * 3600 * 1000)); // deadline: +1 day
        t1.setTags(Arrays.asList("shopping"));
        todoService.addTask(t1);

        Task t2 = new Task("Finish homework", new Date(System.currentTimeMillis() + 2 * 24 * 3600 * 1000)); // +2 days
        t2.setTags(Arrays.asList("school"));
        todoService.addTask(t2);

        Task t3 = new Task("Machine Coding Interview Round", new Date(System.currentTimeMillis() + 3 * 24 * 3600 * 1000)); // +3 days
        t3.setTags(Arrays.asList("Interview", "Jobs"));
        todoService.addTask(t3);

        System.out.println("TASKS ADDED");
        System.out.println("---------------------------------------------------------------------------------------------------");

        // Listing all the tasks
        System.out.println("LISTING TASKS");
        List<Task> allTasks = todoService.listTasks();
        for (Task task : allTasks) {
            System.out.println("Task: " + task.getDetails() + " | Deadline: " + sdf.format(task.getDeadline()) + " | Tags: " + task.getTags() + " | Status: " + task.getStatus());
        }
        System.out.println("---------------------------------------------------------------------------------------------------");

        // Modifying a task
        System.out.println("MODIFYING FIRST TASK");
        Task toModify = allTasks.get(0);
        toModify.setDetails("Buy groceries and cook dinner");
        toModify.setTags(Arrays.asList("shopping", "cooking"));
        todoService.modifyTask(toModify);
        System.out.println("Modified Task: " + todoService.getTask(toModify.getTaskId()).getDetails());
        System.out.println("---------------------------------------------------------------------------------------------------");

        // Removing a task
        System.out.println("REMOVING SECOND TASK");
        Task toRemove = allTasks.get(1);
        todoService.removeTask(toRemove.getTaskId());
        System.out.println("Task with ID " + toRemove.getTaskId() + " removed.");
        System.out.println("---------------------------------------------------------------------------------------------------");

        // Listing tasks again
        System.out.println("LISTING TASKS AFTER REMOVAL");
        List<Task> updatedTasks = todoService.listTasks();
        for (Task task : updatedTasks) {
            System.out.println("Task: " + task.getDetails() + " | Deadline: " + sdf.format(task.getDeadline()) + " | Tags: " + task.getTags() + " | Status: " + task.getStatus());
        }
        System.out.println("---------------------------------------------------------------------------------------------------");

        // Marking a task as completed
        System.out.println("COMPLETING FIRST TASK");
        Task taskToComplete = updatedTasks.get(0);
        taskToComplete.setStatus(TaskStatus.COMPLETED);
        todoService.modifyTask(taskToComplete);
        System.out.println("Completed Task: " + todoService.getTask(taskToComplete.getTaskId()).getStatus());
        System.out.println("---------------------------------------------------------------------------------------------------");

        // Activity log (currently not implemented, will print nothing unless you implement it)
        System.out.println("ACTIVITY LOG");
        todoService.getActivityLog(); // You should implement output in this method if you want to see logs!
        System.out.println("---------------------------------------------------------------------------------------------------");

        // Statistics
        System.out.println("STATISTICS");
        TaskStatistics stats = todoService.getStatistics();
        System.out.println("Added: " + (stats.getAdded() != null ? stats.getAdded().size() : 0));
        System.out.println("Modified: " + (stats.getModified() != null ? stats.getModified().size() : 0));
        System.out.println("Removed: " + (stats.getRemoved() != null ? stats.getRemoved().size() : 0));
        System.out.println("Overdue: " + (stats.getSpilledOverDeadline() != null ? stats.getSpilledOverDeadline().size() : 0));
        System.out.println("---------------------------------------------------------------------------------------------------");
    }
}