package com.example.tasks.core.usecases;

import com.example.tasks.application.TaskRepository;
import com.example.tasks.core.entities.Task;

import java.util.Optional;

/**
 * Class for deleting tasks from given repo
 *
 * @author Dalibor Svonavec
 */
public class DeleteTask {
    private final TaskRepository repo;

    public DeleteTask(TaskRepository repo) {
        if (repo == null) {
            throw new IllegalArgumentException("Repository must be provided");
        }
        this.repo = repo;
    }

    public Task execute(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Task ID is required");
        }
        Optional<Task> taskOptional = repo.findById(id);
        if (taskOptional.isEmpty()) {
            throw new IllegalArgumentException("Task with id = " + id + " was not found");
        }
        repo.delete(id);

        // Internal failure
        if (repo.findById(id).isPresent()) {
            throw new IllegalStateException("Failed to delete task with id " + id);
        }

        return taskOptional.get();
    }
}
