package com.example.tasks.core.usecases;

import com.example.tasks.application.TaskRepository;
import com.example.tasks.core.entities.Task;

import java.util.Optional;

/**
 * Class for changing completion of a task in given repository
 *
 * @author Dalibor Svonavec
 */
public class ToggleTaskCompletion {
    private final TaskRepository repo;

    public ToggleTaskCompletion(TaskRepository repo) {
        if (repo == null) {
            throw new IllegalArgumentException("Repository must be provided");
        }
        this.repo = repo;
    }

    public Task execute(String id) {
        // It would be good to disallow empty ID strings or use other ID type, but since CreateTask allows them,
        // I also allowed them
        if (id == null) {
            throw new IllegalArgumentException("Task ID is required");
        }
        Optional<Task> taskOptional = repo.findById(id);
        if (taskOptional.isEmpty()) {
            throw new IllegalArgumentException("Task with id = " + id + " was not found");
        }
        Task task = taskOptional.get();
        task.toggleCompleted();
        repo.save(task);

        return task.copy();
    }
}
