package com.sanchitp.dev.task.management.system.task.controller;

import com.sanchitp.dev.task.management.system.task.dto.AssignTaskRequest;
import com.sanchitp.dev.task.management.system.task.dto.CreateTaskRequest;
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
    public Task createTask(@Valid @RequestBody CreateTaskRequest request){
        return taskService.creatTask(
                request.getTitle(),
                request.getDescription()
        );
    }

    //Get Task By User I'd
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    //Get All Task
    @GetMapping
    public List<Task> getAllTasks(){
        return taskService.findAllTasks();
    }

    //Assign Task To User
    @PostMapping("/{taskId}/assign")
    public Task assignTask(@PathVariable Long taskId,
                           @Valid @RequestBody AssignTaskRequest request){
        return taskService.assignTaskToUser(taskId,request.getUserId());
    }

    //Update Task Status
    @PatchMapping("/{id}/status")
    public Task updateTaskStatus(@PathVariable Long taskId,
                                 @Valid @RequestBody UpdateTaskStatusRequest request)
    {
        return taskService.updateTaskStatus(taskId,request.getStatus());
    }

    //Get User By Task
    @GetMapping("/user/{userId}")
    public List<Task> getTaskByUserId(@PathVariable Long userId){
        return taskService.getTaskByUser(userId);
    }
}

