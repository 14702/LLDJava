package com.ToDo.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException() { super(); }
    public TaskNotFoundException(String message) { super(message); }
}
