package com.stefanini.todo.api;

import com.stefanini.todo.api.dto.TaskRequest;
import com.stefanini.todo.api.dto.TaskResponse;
import com.stefanini.todo.api.mapper.TaskApiMapper;
import com.stefanini.todo.application.service.TaskService;
import com.stefanini.todo.domain.model.Task;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request, UriComponentsBuilder uriBuilder) {
        Task created = taskService.create(TaskApiMapper.toDomain(request));
        URI location = uriBuilder.path("/tasks/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(TaskApiMapper.toResponse(created));
    }

    @GetMapping
    public List<TaskResponse> list() {
        return taskService.findAll().stream()
                .map(TaskApiMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public TaskResponse findById(@PathVariable UUID id) {
        return TaskApiMapper.toResponse(taskService.findById(id));
    }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable UUID id, @Valid @RequestBody TaskRequest request) {
        Task updated = taskService.update(id, TaskApiMapper.toDomain(request));
        return TaskApiMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
