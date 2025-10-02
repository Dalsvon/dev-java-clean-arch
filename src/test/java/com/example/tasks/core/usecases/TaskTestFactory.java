package com.example.tasks.core.usecases;

import com.example.tasks.core.entities.Task;

public class TaskTestFactory {
    public static Task taskWithId1() {
        return new Task("1", "Title1");
    }

    public static Task taskWithIdString() {
        return new Task("String", "TitleWithIdString");
    }

    public static Task taskCompleted() {
        Task task = new Task("42", "Completed");
        task.toggleCompleted();
        return task;
    }
}
