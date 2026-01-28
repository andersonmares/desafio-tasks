package com.stefanini.todo.application.service;

import com.stefanini.todo.domain.exception.TaskNotFoundException;
import com.stefanini.todo.domain.model.Task;
import com.stefanini.todo.domain.model.TaskStatus;
import com.stefanini.todo.domain.repository.TaskRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task create(Task task) {
        TaskStatus status = task.getStatus() == null ? TaskStatus.PENDING : task.getStatus();
        LocalDateTime createdAt = task.getCreatedAt() == null ? LocalDateTime.now() : task.getCreatedAt();
        Task toPersist = Task.newTask(task.getTitle(), task.getDescription(), status, createdAt);
        return taskRepository.save(toPersist);
    }

    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Task findById(UUID id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task update(UUID id, Task updatedTask) {
        Task existing = findById(id);
        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setStatus(updatedTask.getStatus());
        return taskRepository.save(existing);
    }

    public void delete(UUID id) {
        Task existing = findById(id);
        taskRepository.deleteById(existing.getId());
    }
}
