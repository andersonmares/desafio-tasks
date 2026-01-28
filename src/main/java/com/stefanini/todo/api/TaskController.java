package com.stefanini.todo.api;

import com.stefanini.todo.api.dto.TaskRequest;
import com.stefanini.todo.api.dto.TaskResponse;
import com.stefanini.todo.api.mapper.TaskApiMapper;
import com.stefanini.todo.application.service.TaskService;
import com.stefanini.todo.domain.model.Task;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Tasks", description = "Operações para gerenciamento de tarefas")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @Operation(summary = "Cria uma nova tarefa")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarefa criada",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request, UriComponentsBuilder uriBuilder) {
        Task created = taskService.create(TaskApiMapper.toDomain(request));
        URI location = uriBuilder.path("/tasks/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(TaskApiMapper.toResponse(created));
    }

    @GetMapping
    @Operation(summary = "Lista todas as tarefas")
    public List<TaskResponse> list() {
        return taskService.findAll().stream()
                .map(TaskApiMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma tarefa pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content)
    })
    public TaskResponse findById(@PathVariable UUID id) {
        return TaskApiMapper.toResponse(taskService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma tarefa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content)
    })
    public TaskResponse update(@PathVariable UUID id, @Valid @RequestBody TaskRequest request) {
        Task updated = taskService.update(id, TaskApiMapper.toDomain(request));
        return TaskApiMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma tarefa")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa removida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content)
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
