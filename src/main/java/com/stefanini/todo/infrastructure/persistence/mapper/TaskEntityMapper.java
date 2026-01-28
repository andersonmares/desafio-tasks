package com.stefanini.todo.infrastructure.persistence.mapper;

import com.stefanini.todo.domain.model.Task;
import com.stefanini.todo.infrastructure.persistence.entity.TaskEntity;

public final class TaskEntityMapper {

    private TaskEntityMapper() {
    }

    public static TaskEntity toEntity(Task task) {
        TaskEntity entity = new TaskEntity();
        entity.setId(task.getId());
        entity.setTitle(task.getTitle());
        entity.setDescription(task.getDescription());
        entity.setCreatedAt(task.getCreatedAt());
        entity.setStatus(task.getStatus());
        return entity;
    }

    public static Task toDomain(TaskEntity entity) {
        return new Task(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getStatus()
        );
    }
}
