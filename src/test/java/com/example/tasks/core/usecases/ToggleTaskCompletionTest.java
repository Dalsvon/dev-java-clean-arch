package com.example.tasks.core.usecases;

import com.example.tasks.application.TaskRepository;
import com.example.tasks.core.entities.Task;
import com.example.tasks.infrastructure.InMemoryTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToggleTaskCompletionTest {
    private TaskRepository repo;
    private ToggleTaskCompletion useCase;

    @BeforeEach
    void setUp() {
        repo = new InMemoryTaskRepository();
        useCase = new ToggleTaskCompletion(repo);
    }

    @Test
    void toggleCompletionTask_noTaskFound_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute("1");
        });
        assertEquals("Task with id = 1 was not found", exception.getMessage());
    }

    @Test
    void toggleCompletionTask_uncompletedTaskFound_returnsCompletedTask() {
        repo.save(TaskTestFactory.taskWithId1());
        repo.save(TaskTestFactory.taskCompleted());
        repo.save(TaskTestFactory.taskWithIdString());

        Task task = useCase.execute("1");
        assertTrue(task.isCompleted());
        assertEquals("1", task.getId());
        assertTrue(repo.findById("1").isPresent());
        assertTrue(repo.findById("1").get().isCompleted());
        assertEquals(3, repo.findAll().size());
    }

    @Test
    void toggleCompletionTask_completedTaskFound_returnsUncompletedTask() {
        Task completed = TaskTestFactory.taskCompleted();
        repo.save(TaskTestFactory.taskWithId1());
        repo.save(completed);

        String id = completed.getId();

        Task task = useCase.execute(id);
        assertFalse(task.isCompleted());
        assertEquals(id, task.getId());
        assertTrue(repo.findById(id).isPresent());
        assertFalse(repo.findById(id).get().isCompleted());
        assertEquals(2, repo.findAll().size());
    }

    @Test
    void toggleCompletionTask_toggledTwice_returnsToOriginalState() {
        Task task = TaskTestFactory.taskWithId1();
        repo.save(task);
        boolean originalState = task.isCompleted();

        Task firstToggle = useCase.execute("1");
        assertEquals(!originalState, firstToggle.isCompleted());

        Task secondToggle = useCase.execute("1");
        assertEquals(originalState, secondToggle.isCompleted());
    }

    @Test
    void toggleCompletionTask_noId_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute(null);
        });
        assertEquals("Task ID is required", exception.getMessage());
    }

    @Test
    void constructor_nullRepository_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new ToggleTaskCompletion(null));

        assertEquals("Repository must be provided", exception.getMessage());
    }
}
