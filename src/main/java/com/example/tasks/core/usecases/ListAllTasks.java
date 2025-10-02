package com.example.tasks.core.usecases;

import com.example.tasks.application.TaskRepository;
import com.example.tasks.core.entities.Task;

import java.util.Collections;
import java.util.List;

/**
 * Lists all tasks in given repository
 *
 * @author Dalibor Svonavec
 */
public class ListAllTasks {
    private final TaskRepository repo;

    public ListAllTasks(TaskRepository repo) {
        if (repo == null) {
            throw new IllegalArgumentException("Repository must be provided");
        }
        this.repo = repo;
    }

    public List<Task> execute() {
        List<Task> tasks = repo.findAll();
        if (tasks == null) {
            return Collections.emptyList();
        }
        List<Task> safeCopy = tasks
                .stream()
                .map(Task::copy)
                .toList();
        return safeCopy;
    }
}
