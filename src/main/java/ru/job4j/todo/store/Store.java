package ru.job4j.todo.store;

import ru.job4j.todo.model.Task;

import java.util.List;

public interface Store extends AutoCloseable {
    Task add(Task item);

    List<Task> findAll();

    List<Task> findByUndone();

    Task findById(String key);

    boolean done(Task task);
}
