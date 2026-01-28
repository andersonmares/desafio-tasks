package com.stefanini.todo.infrastructure.persistence;

import com.stefanini.todo.domain.model.Task;
import com.stefanini.todo.domain.repository.TaskRepository;
import com.stefanini.todo.infrastructure.persistence.entity.TaskEntity;
import com.stefanini.todo.infrastructure.persistence.jpa.SpringDataTaskRepository;
import com.stefanini.todo.infrastructure.persistence.mapper.TaskEntityMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class TaskRepositoryImpl implements TaskRepository {

    private final SpringDataTaskRepository springDataTaskRepository;

    public TaskRepositoryImpl(SpringDataTaskRepository springDataTaskRepository) {
        this.springDataTaskRepository = springDataTaskRepository;
    }

    @Override
    public Task save(Task task) {
        TaskEntity entity = TaskEntityMapper.toEntity(task);
        TaskEntity saved = springDataTaskRepository.save(entity);
        return TaskEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return springDataTaskRepository.findById(id).map(TaskEntityMapper::toDomain);
    }

    @Override
    public List<Task> findAll() {
        return springDataTaskRepository.findAll().stream()
                .map(TaskEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        springDataTaskRepository.deleteById(id);
    }
}
