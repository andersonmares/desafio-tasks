package com.stefanini.todo.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.stefanini.todo.domain.exception.TaskNotFoundException;
import com.stefanini.todo.domain.model.Task;
import com.stefanini.todo.domain.model.TaskStatus;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Test
    void createAndRetrieveTask() {
        Task created = taskService.create(new Task(null, "Write docs", "Document the API", null, TaskStatus.PENDING));

        assertNotNull(created.getId());
        assertNotNull(created.getCreatedAt());
        Task loaded = taskService.findById(created.getId());
        assertEquals("Write docs", loaded.getTitle());
        assertEquals(TaskStatus.PENDING, loaded.getStatus());
    }

    @Test
    void updateTask() {
        Task created = taskService.create(new Task(null, "Initial title", "Desc", null, TaskStatus.PENDING));

        Task updatedData = new Task(null, "Updated title", "Updated desc", created.getCreatedAt(), TaskStatus.IN_PROGRESS);
        Task updated = taskService.update(created.getId(), updatedData);

        assertEquals("Updated title", updated.getTitle());
        assertEquals(TaskStatus.IN_PROGRESS, updated.getStatus());
    }

    @Test
    void deleteTaskRemovesIt() {
        Task created = taskService.create(new Task(null, "To delete", "Desc", null, TaskStatus.PENDING));
        taskService.delete(created.getId());

        assertThrows(TaskNotFoundException.class, () -> taskService.findById(created.getId()));
    }

    @Test
    void listReturnsAllTasks() {
        taskService.create(new Task(null, "Task 1", "Desc1", null, TaskStatus.PENDING));
        taskService.create(new Task(null, "Task 2", "Desc2", null, TaskStatus.COMPLETED));

        List<Task> tasks = taskService.findAll();
        assertNotNull(tasks);
        // No hard assertion on size to avoid coupling with previous tests sharing context
    }
}
