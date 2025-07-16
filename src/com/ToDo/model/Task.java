package com.ToDo.model;
import com.ToDo.enums.TaskStatus;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Task {
    private String taskId;
    private String details;
    private Date deadline;
    private Date createdAt;
    private Date updatedAt;
    private List<String> tags;
    private TaskStatus status;

    public Task(String taskId, String details, Date deadline, TaskStatus status) {
        this.taskId = taskId;
        this.details = details;
        this.deadline = deadline;
        this.status = status;
    }

    public Task(String taskId, String details, Date deadline) {
        this.taskId = taskId;
        this.details = details;
        this.deadline = deadline;
        this.status = TaskStatus.SCHEDULED;
    }

    public Task(String details, Date deadline) {
        this.taskId = UUID.randomUUID().toString();
        this.details = details;
        this.deadline = deadline;
        this.status = TaskStatus.SCHEDULED;
    }

    public Task(String details) {
        this.taskId = UUID.randomUUID().toString();
        this.details = details;
        this.status = TaskStatus.SCHEDULED;
    }


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
