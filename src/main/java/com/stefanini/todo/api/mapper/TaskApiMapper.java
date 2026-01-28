package com.stefanini.todo.api.mapper;

import com.stefanini.todo.api.dto.TaskRequest;
import com.stefanini.todo.api.dto.TaskResponse;
import com.stefanini.todo.domain.model.Task;

public final class TaskApiMapper {

    private TaskApiMapper() {
    }

    public static Task toDomain(TaskRequest request) {
        return new Task(
                null,
                request.getTitle(),
                request.getDescription(),
                null,
                request.getStatus()
        );
    }

    public static TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreatedAt(),
                task.getStatus()
        );
    }
}
