package com.ToDo;

import com.ToDo.repository.TaskRepositoryImpl;
import com.ToDo.repository.TaskRepository;
import com.ToDo.service.TodoService;
import com.ToDo.service.TodoServiceImpl;

import java.util.Scanner;

public class ToDoApplication {

    public static void main(String[] args) {
        TaskRepository taskRepository = new TaskRepositoryImpl();
        TodoService todoService = new TodoServiceImpl(taskRepository);



    }
}
