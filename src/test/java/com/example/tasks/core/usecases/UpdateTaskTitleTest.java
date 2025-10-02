package com.example.tasks.core.usecases;

import com.example.tasks.application.TaskRepository;
import com.example.tasks.core.entities.Task;
import com.example.tasks.infrastructure.InMemoryTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateTaskTitleTest {

    private TaskRepository repo;
    private UpdateTaskTitle useCase;

    @BeforeEach
    void setUp() {
        repo = new InMemoryTaskRepository();
        useCase = new UpdateTaskTitle(repo);
    }

    @Test
    void updateTask_noTaskFound_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute("1", "Title");
        });
        assertEquals("Task with id = 1 was not found", exception.getMessage());
    }

    @Test
    void updateTask_correctTaskFound_returnsTask() {
        repo.save(TaskTestFactory.taskWithId1());
        repo.save(TaskTestFactory.taskCompleted());
        repo.save(TaskTestFactory.taskWithIdString());

        Task task = useCase.execute("1", "Updated title");
        assertEquals("Updated title", task.getTitle());
        assertEquals("1", task.getId());
        assertTrue(repo.findById("1").isPresent());
        assertEquals("Updated title", repo.findById("1").get().getTitle());
    }

    @Test
    void updateTask_noTitle_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute("1", null);
        });
        assertEquals("Title cannot be empty", exception.getMessage());
    }

    @Test
    void updateTask_titleEmpty_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute("1", "   ");
        });
        assertEquals("Title cannot be empty", exception.getMessage());
    }

    @Test
    void updateTask_noId_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute(null, "Title");
        });
        assertEquals("Task ID is required", exception.getMessage());
    }

    @Test
    void updateTask_taskUpdated_returnsTaskSafeToEdit() {
        repo.save(TaskTestFactory.taskWithId1());

        Task task = useCase.execute("1", "New title");

        task.setTitle("Title1");

        Task fromRepo = repo.findById("1").orElseThrow();

        assertEquals("New title", fromRepo.getTitle());
    }

    @Test
    void constructor_nullRepository_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new UpdateTaskTitle(null));

        assertEquals("Repository must be provided", exception.getMessage());
    }

    @Test
    void updateTask_specialCharactersInTitle_updatesSuccessfully() {
        repo.save(TaskTestFactory.taskWithId1());
        String title = "         <script>alert('xss')</script> & 1232      ".repeat(50);

        Task updated = useCase.execute("1", title);

        assertEquals(title, updated.getTitle());
    }
}
