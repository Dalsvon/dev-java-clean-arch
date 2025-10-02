package com.example.tasks.core.usecases;

import com.example.tasks.application.TaskRepository;
import com.example.tasks.core.entities.Task;
import com.example.tasks.infrastructure.InMemoryTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteTaskTest {
    private TaskRepository repo;
    private DeleteTask useCase;

    @BeforeEach
    void setUp() {
        repo = new InMemoryTaskRepository();
        useCase = new DeleteTask(repo);
    }

    @Test
    void deleteTask_noTaskFound_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute("1");
        });
        assertEquals("Task with id = 1 was not found", exception.getMessage());
    }

    @Test
    void deleteTask_correctTaskFound_deletesTask() {
        repo.save(TaskTestFactory.taskWithId1());
        repo.save(TaskTestFactory.taskCompleted());
        repo.save(TaskTestFactory.taskWithIdString());

        Task task = useCase.execute("1");
        assertEquals("1", task.getId());
        assertTrue(repo.findById("1").isEmpty());

        assertEquals(repo.findAll().size(), 2);
        assertTrue(repo.findById("String").isPresent());
    }

    @Test
    void deleteTask_completedTaskFound_deletesTask() {
        Task complededTask = TaskTestFactory.taskCompleted();
        repo.save(TaskTestFactory.taskWithId1());
        repo.save(complededTask);
        repo.save(TaskTestFactory.taskWithIdString());

        String id = complededTask.getId();

        Task task = useCase.execute(id);
        assertEquals(id, task.getId());
        assertTrue(repo.findById(id).isEmpty());

        assertTrue(task.isCompleted());
        assertTrue(repo.findById(task.getId()).isEmpty());
    }

    @Test
    void deleteTask_noId_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute(null);
        });
        assertEquals("Task ID is required", exception.getMessage());
    }

    @Test
    void constructor_nullRepository_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new DeleteTask(null));

        assertEquals("Repository must be provided", exception.getMessage());
    }

    @Test
    void deleteTask_deletingSameTwice_throwsException() {
        repo.save(TaskTestFactory.taskWithId1());

        useCase.execute("1");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> useCase.execute("1"));

        assertTrue(exception.getMessage().contains("not found"));
    }
}
