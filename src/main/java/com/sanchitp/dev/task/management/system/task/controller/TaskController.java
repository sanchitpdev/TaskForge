package com.sanchitp.dev.task.management.system.task.controller;

import com.sanchitp.dev.task.management.system.task.dto.AssignTaskRequest;
import com.sanchitp.dev.task.management.system.task.dto.CreateTaskRequest;
import com.sanchitp.dev.task.management.system.task.dto.TaskResponse;
import com.sanchitp.dev.task.management.system.task.dto.UpdateTaskStatusRequest;
import com.sanchitp.dev.task.management.system.task.entity.Task;
import com.sanchitp.dev.task.management.system.task.service.TaskService;
import com.sanchitp.dev.task.management.system.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //Create Task
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request){
        return taskService.createTask(
                request.getTitle(),
                request.getDescription()
        );
    }

    //Get Task By User I'd
    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    //Get All Task
    @GetMapping
    public List<TaskResponse> getAllTasks(){
        return taskService.findAllTasks();
    }

    //Assign Task To User
    @PostMapping("/{taskId}/assign")
    public TaskResponse assignTask(@PathVariable Long taskId,
                           @Valid @RequestBody AssignTaskRequest request){
        return taskService.assignTaskToUser(taskId,request.getUserId());
    }

    //Update Task Status
    @PatchMapping("/{id}/status")
    public TaskResponse updateTaskStatus(@PathVariable Long taskId,
                                 @Valid @RequestBody UpdateTaskStatusRequest request)
    {
        return taskService.updateTaskStatus(taskId,request.getStatus());
    }

    //Get User By Task
    @GetMapping("/user/{userId}")
    public List<TaskResponse> getTaskByUserId(@PathVariable Long userId){
        return taskService.getTaskByUser(userId);
    }
}

