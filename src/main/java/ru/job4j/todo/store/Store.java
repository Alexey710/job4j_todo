package ru.job4j.todo.store;

import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.util.List;

public interface Store extends AutoCloseable {
    Task add(Task item);

    List<Task> findAll(User user);

    List<Task> findByUndone(User user);

    Task findById(String key);

    boolean done(Task task);

    User findByCredential(String email, String password);

    User addUser(User user);
}
