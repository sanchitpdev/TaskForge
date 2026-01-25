package com.sanchitp.dev.task.management.system.task.controller;

import com.sanchitp.dev.task.management.system.security.util.SecurityUtils;
import com.sanchitp.dev.task.management.system.task.dto.TaskResponse;
import com.sanchitp.dev.task.management.system.task.service.TaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users/tasks")
@PreAuthorize("hasRole('USER')")
public class UserTaskController {

    private final TaskService taskService;

    public UserTaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponse> getMyTasks() {
        Long userId = SecurityUtils.getCurrentUser().getUserId();
        return taskService.getTaskByUser(userId);
    }

    @PatchMapping("/{taskId}/ready")
    public TaskResponse markReadyForReview(@PathVariable Long taskId) {
        return taskService.markTaskReadyForReview(taskId);
    }

}
