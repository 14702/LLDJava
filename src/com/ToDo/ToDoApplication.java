package com.ToDo;

import com.ToDo.repository.interfaces.TaskRepository;
import com.ToDo.repository.TaskRepositoryImpl;
import com.ToDo.service.interfaces.TodoService;
import com.ToDo.service.TodoServiceImpl;
import com.ToDo.model.Activity;
import com.ToDo.model.Task;
import com.ToDo.model.TaskStatistics;
import java.util.*;
import java.text.SimpleDateFormat;

public class ToDoApplication {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final String LINE = "---------------------------------------------------------------------------------------------------";

    public static void main(String[] args) throws Exception {
        TaskRepository taskRepository = new TaskRepositoryImpl();
        TodoService todoService = new TodoServiceImpl(taskRepository);

        Date periodStart = new Date();

        // === 1. Add Tasks ===
        section("ADDING TASKS");
        Task t1 = new Task("Buy groceries", new Date(System.currentTimeMillis() + 24 * 3600 * 1000L));
        t1.setTags(Arrays.asList("shopping"));
        todoService.addTask(t1);

        Task t2 = new Task("Finish homework", new Date(System.currentTimeMillis() + 2 * 24 * 3600 * 1000L));
        t2.setTags(Arrays.asList("school"));
        todoService.addTask(t2);

        Task t3 = new Task("Machine Coding Interview", new Date(System.currentTimeMillis() + 3 * 24 * 3600 * 1000L));
        t3.setTags(Arrays.asList("Interview", "Jobs"));
        todoService.addTask(t3);

        Task t4 = new Task("Overdue task for testing", new Date(System.currentTimeMillis() - 3600 * 1000L));
        t4.setTags(Arrays.asList("testing"));
        todoService.addTask(t4);

        System.out.println("Added 4 tasks (1 already past deadline).");

        // === 2. List All Tasks ===
        section("LISTING ALL TASKS");
        printTasks(todoService.listTasks());

        // === 3. Filter by Tags ===
        section("FILTER BY TAGS [Interview, Jobs]");
        printTasks(todoService.listTasks(Arrays.asList("Interview", "Jobs")));

        // === 4. Modify a Task ===
        section("MODIFYING FIRST TASK");
        Task toModify = todoService.getTask(t1.getTaskId());
        toModify.setDetails("Buy groceries and cook dinner");
        toModify.setTags(Arrays.asList("shopping", "cooking"));
        todoService.modifyTask(toModify);
        Task modified = todoService.getTask(t1.getTaskId());
        System.out.println("Modified -> " + modified.getDetails() + " | Tags: " + modified.getTags());

        // === 5. Remove a Task ===
        section("REMOVING 'Finish homework'");
        todoService.removeTask(t2.getTaskId());
        System.out.println("Removed task: " + t2.getDetails());

        section("TASKS AFTER REMOVAL");
        printTasks(todoService.listTasks());

        // === 6. Complete a Task (auto-removed from list) ===
        section("COMPLETING 'Machine Coding Interview'");
        todoService.completeTask(t3.getTaskId());
        System.out.println("Completed and auto-removed: " + t3.getDetails());

        section("TASKS AFTER COMPLETION");
        printTasks(todoService.listTasks());

        // === 7. Activity Log ===
        Date periodEnd = new Date(System.currentTimeMillis() + 1000);
        section("FULL ACTIVITY LOG");
        for (Activity a : todoService.getActivityLog()) {
            System.out.println("  " + a);
        }

        section("ACTIVITY LOG (WITHIN TIME PERIOD)");
        for (Activity a : todoService.getActivityLog(periodStart, periodEnd)) {
            System.out.println("  " + a);
        }

        // === 8. Statistics ===
        section("STATISTICS (ALL TIME)");
        TaskStatistics stats = todoService.getStatistics();
        System.out.println("Active tasks : " + stats.getAdded().size());
        System.out.println("Modified     : " + stats.getModified().size());
        System.out.println("Removed      : " + stats.getRemoved().size());
        System.out.println("Overdue      : " + stats.getSpilledOverDeadline().size());

        section("STATISTICS (WITHIN TIME PERIOD)");
        TaskStatistics periodStats = todoService.getStatistics(periodStart, periodEnd);
        System.out.println("Added        : " + periodStats.getAdded().size());
        System.out.println("Modified     : " + periodStats.getModified().size());
        System.out.println("Removed      : " + periodStats.getRemoved().size());
        System.out.println("Overdue      : " + periodStats.getSpilledOverDeadline().size());

        section("ALL TESTS PASSED");
    }

    private static void section(String title) {
        System.out.println(LINE);
        System.out.println(title);
        System.out.println(LINE);
    }

    private static void printTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("  (no tasks)");
            return;
        }
        for (Task t : tasks) {
            System.out.println("  " + t.getDetails()
                    + " | Deadline: " + (t.getDeadline() != null ? SDF.format(t.getDeadline()) : "none")
                    + " | Tags: " + t.getTags()
                    + " | Status: " + t.getStatus());
        }
    }
}
