package com.example.tasks.core.entities;

public class Task {
    private final String id;
    private String title;
    private boolean completed;

    public Task(String id, String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.id = id;
        this.title = title;
        this.completed = false;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title;
    }
    public boolean isCompleted() { return completed; }
    public void toggleCompleted() { this.completed = !this.completed; }

    // I added this method to create safe copies to avoid unintended changes in the database
    public Task copy() {
        Task task = new Task(getId(), getTitle());
        task.completed = completed;
        return task;
    }
}
