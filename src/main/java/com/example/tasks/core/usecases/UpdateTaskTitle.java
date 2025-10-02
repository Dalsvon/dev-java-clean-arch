package com.example.tasks.core.usecases;

import com.example.tasks.application.TaskRepository;
import com.example.tasks.core.entities.Task;

import java.util.Optional;

/**
 * Class for updating title of a task in given repository
 *
 * @author Dalibor Svonavec
 */
public class UpdateTaskTitle {
    private final TaskRepository repo;

    public UpdateTaskTitle(TaskRepository repo) {
        if (repo == null) {
            throw new IllegalArgumentException("Repository must be provided");
        }
        this.repo = repo;
    }

    public Task execute(String id, String title) {
        // It would be good to disallow empty ID strings or use other ID type, but since CreateTask allows them,
        // I also allowed them
        if (id == null) {
            throw new IllegalArgumentException("Task ID is required");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        Optional<Task> taskOptional = repo.findById(id);
        if (taskOptional.isEmpty()) {
            throw new IllegalArgumentException("Task with id = " + id + " was not found");
        }
        Task task = taskOptional.get();
        task.setTitle(title);
        repo.save(task);

        return task.copy();
    }
}
