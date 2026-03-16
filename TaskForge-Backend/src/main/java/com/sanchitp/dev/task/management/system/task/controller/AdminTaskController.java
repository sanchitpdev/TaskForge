package com.sanchitp.dev.task.management.system.task.controller;

import com.sanchitp.dev.task.management.system.task.dto.AssignTaskRequest;
import com.sanchitp.dev.task.management.system.task.dto.CreateTaskRequest;
import com.sanchitp.dev.task.management.system.task.dto.TaskResponse;
import com.sanchitp.dev.task.management.system.task.dto.UpdateTaskStatusRequest;
import com.sanchitp.dev.task.management.system.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tasks")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTaskController {

    private final TaskService taskService;

    public AdminTaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(request.getTitle(), request.getDescription());
    }

    @GetMapping
    public List<TaskResponse> getAllTasks() {
        return taskService.findAllTasks();
    }

    @PatchMapping("/{taskId}/assign")
    public TaskResponse assignTask(@PathVariable Long taskId,
                                   @Valid @RequestBody AssignTaskRequest request) {
        return taskService.assignTaskToUser(taskId, request.getUserId());
    }

    @PatchMapping("/{taskId}/status")
    public TaskResponse updateTaskStatus(@PathVariable Long taskId,
                                         @Valid @RequestBody UpdateTaskStatusRequest request) {
        return taskService.updateTaskStatus(taskId, request.getStatus());
    }

    @PatchMapping("/{taskId}/complete")
    public TaskResponse completeTask(@PathVariable Long taskId) {
        return taskService.completeTask(taskId);
    }

    @PutMapping("/{taskId}/approve")
    public ResponseEntity<Void> approveTask(@PathVariable Long taskId) {
        taskService.approveTask(taskId);
        return ResponseEntity.ok().build();
    }
}
