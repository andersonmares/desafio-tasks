package com.stefanini.todo.domain.repository;

import com.stefanini.todo.domain.model.Task;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {
    Task save(Task task);

    Optional<Task> findById(UUID id);

    List<Task> findAll();

    void deleteById(UUID id);
}
