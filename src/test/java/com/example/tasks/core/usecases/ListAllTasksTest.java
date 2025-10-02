package com.example.tasks.core.usecases;

import com.example.tasks.application.TaskRepository;
import com.example.tasks.core.entities.Task;
import com.example.tasks.infrastructure.InMemoryTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListAllTasksTest {
    private TaskRepository repo;
    private ListAllTasks useCase;

    private String title = "Title1";

    @BeforeEach
    void setUp() {
        repo = new InMemoryTaskRepository();
        useCase = new ListAllTasks(repo);
    }

    @Test
    void listAllTasks_noTaskFound_returnsEmptyList() {
        List<Task> tasks = useCase.execute();

        assertEquals(0, tasks.size());
    }

    @Test
    void listAllTasks_threeTasksFound_returnsTasks() {
        repo.save(TaskTestFactory.taskWithId1());
        repo.save(TaskTestFactory.taskCompleted());
        repo.save(TaskTestFactory.taskWithIdString());

        List<Task> tasks = useCase.execute();
        assertEquals(3, tasks.size());
        assertTrue(tasks.stream().anyMatch(t -> t.getId().equals("1")));
        assertTrue(tasks.stream().anyMatch(t -> t.getTitle().equals(title)));
        assertTrue(tasks.stream().anyMatch(t -> t.getId().equals("42")));
        assertTrue(tasks.stream().anyMatch(t -> t.getId().equals("String")));
        assertEquals(3, repo.findAll().size());
    }

    @Test
    void listAllTasks_oneTaskFound_returnsUnmodifiedTask() {
        repo.save(TaskTestFactory.taskWithId1());

        List<Task> tasks = useCase.execute();
        assertEquals(1, tasks.size());
        assertEquals("1", tasks.get(0).getId());
        assertEquals(title, tasks.get(0).getTitle());
        assertFalse(tasks.get(0).isCompleted());
        assertEquals(1, repo.findAll().size());

        assertTrue(repo.findById("1").isPresent());
        assertEquals(title, repo.findById("1").get().getTitle());
        assertFalse(repo.findById("1").get().isCompleted());
    }

    @Test
    void listAllTasks_manyTasksFound_returnsTasks() {
        for (int i = 0; i < 1000; i++) {
            repo.save(new Task(String.valueOf(i), "title"));
        }

        List<Task> tasks = useCase.execute();
        assertEquals(1000, tasks.size());
        assertTrue(tasks.stream().anyMatch(t -> t.getId().equals("1")));
    }

    @Test
    void listAllTasks_tasksFound_returnsUnmodifiableTest() {
        repo.save(TaskTestFactory.taskWithId1());

        assertThrows(UnsupportedOperationException.class,
                () -> useCase.execute().add(TaskTestFactory.taskCompleted()));

        assertThrows(UnsupportedOperationException.class,
                () -> useCase.execute().remove(TaskTestFactory.taskWithId1()));
    }

    @Test
    void listAllTasks_tasksFound_returnsListWithObjectsSafeToEdit() {
        repo.save(TaskTestFactory.taskWithId1());

        List<Task> tasks = useCase.execute();
        Task returnedTask = tasks.get(0);

        returnedTask.setTitle("Changed title");

        Task fromRepo = repo.findById("1").orElseThrow();

        assertEquals(title, fromRepo.getTitle());
    }

    @Test
    void constructor_nullRepository_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new ListAllTasks(null));

        assertEquals("Repository must be provided", exception.getMessage());
    }
}
