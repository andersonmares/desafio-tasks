package com.stefanini.todo.infrastructure.persistence.jpa;

import com.stefanini.todo.infrastructure.persistence.entity.TaskEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataTaskRepository extends JpaRepository<TaskEntity, UUID> {
}
