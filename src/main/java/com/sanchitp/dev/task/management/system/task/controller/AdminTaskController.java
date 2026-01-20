package com.sanchitp.dev.task.management.system.task.controller;

import com.sanchitp.dev.task.management.system.task.dto.*;
import com.sanchitp.dev.task.management.system.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/tasks")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTaskController {

    private final TaskService taskService;

    public AdminTaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(
                request.getTitle(),
                request.getDescription()
        );
    }

    @PostMapping("/{taskId}/assign")
    public TaskResponse assignTask(@PathVariable Long taskId,
                                   @Valid @RequestBody AssignTaskRequest request) {
        return taskService.assignTaskToUser(taskId, request.getUserId());
    }

    @PatchMapping("/{taskId}/status")
    public TaskResponse updateTaskStatus(@PathVariable Long taskId,
                                         @Valid @RequestBody UpdateTaskStatusRequest request) {
        return taskService.updateTaskStatus(taskId, request.getStatus());
    }

    @GetMapping
    public List<TaskResponse> getAllTasks() {
        return taskService.findAllTasks();
    }
}
