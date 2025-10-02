package com.example.tasks.infrastructure;

import com.example.tasks.application.TaskRepository;
import com.example.tasks.core.entities.Task;
import java.util.*;

public class InMemoryTaskRepository implements TaskRepository {
    private final Map<String, Task> tasks = new HashMap<>();

    @Override
    public void save(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public Optional<Task> findById(String id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void delete(String id) {
        tasks.remove(id);
    }
}
