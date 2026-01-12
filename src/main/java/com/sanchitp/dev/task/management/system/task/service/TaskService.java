package com.sanchitp.dev.task.management.system.task.service;

import com.sanchitp.dev.task.management.system.common.enums.TaskStatus;
import com.sanchitp.dev.task.management.system.common.enums.exception.TaskNotFoundException;
import com.sanchitp.dev.task.management.system.common.enums.exception.UserNotFoundException;
import com.sanchitp.dev.task.management.system.task.dto.TaskResponse;
import com.sanchitp.dev.task.management.system.task.entity.Task;
import com.sanchitp.dev.task.management.system.task.repository.TaskRepository;
import com.sanchitp.dev.task.management.system.user.entity.User;
import com.sanchitp.dev.task.management.system.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    /* ===================== HELPERS ===================== */

    private Task getTaskEntityById(Long id){
        return taskRepository.findById(id).orElseThrow(()->new TaskNotFoundException(id));
    }

    private TaskResponse toResponse(Task task){
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getTaskStatus(),
                task.getAssignedTo() != null ? task.getAssignedTo().getId() : null
        );
    }

    /* ===================== CRUD ===================== */

    public TaskResponse createTask(String title ,String description){
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setTaskStatus(TaskStatus.CREATED);
        return toResponse(taskRepository.save(task));
    }

    public TaskResponse assignTaskToUser(Long taskId, Long userId){
        Task task = getTaskEntityById(taskId);
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));

        task.setAssignedTo(user);
        task.setTaskStatus(TaskStatus.ASSIGNED);

        return toResponse(taskRepository.save(task)
        );
    }

    public TaskResponse getTaskById(Long id){
        return toResponse(getTaskEntityById(id));
    }

    public List<TaskResponse> findAllTasks(){
        return taskRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<TaskResponse> getTaskByUser(Long userId){
        User user =  userRepository.findById(userId).orElseThrow(()->new UserNotFoundException(userId));

        return taskRepository.findByAssignedTo(user).stream().map(this::toResponse).toList();
    }

    public TaskResponse updateTaskStatus(Long taskId,TaskStatus status){
        Task task = getTaskEntityById(taskId);
        task.setTaskStatus(status);
        return toResponse(taskRepository.save(task));
    }
}
