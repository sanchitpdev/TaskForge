package com.sanchitp.dev.task.management.system.task.service;

import com.sanchitp.dev.task.management.system.common.enums.TaskStatus;
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

    public Task creatTask(String title ,String description){
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setTaskStatus(TaskStatus.CREATED);
        return taskRepository.save(task);
    }

    public Task assignTaskToUser(Long taskId, Long userId){
        Task task = taskRepository.findById(taskId).orElseThrow(()-> new RuntimeException("Task not Found"));
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User Not Found"));

        task.setAssignedTo(user);
        task.setTaskStatus(TaskStatus.ASSIGNED);

        return taskRepository.save(task);
    }

    public Task getTaskById(Long id){
        return taskRepository.findById(id).orElseThrow(()->new RuntimeException("Task Not Found"));
    }

    public List<Task> findAllTasks(){
        return taskRepository.findAll();
    }

    public List<Task> getTaskByUser(Long userId){
        User user =  userRepository.findById(userId).orElseThrow(()->new RuntimeException("User Not Found"));

        return taskRepository.findByAssignedTo(user);
    }

    public Task updateTaskStatus(Long taskId,TaskStatus status){
        Task task = taskRepository.findById(taskId).orElseThrow(()-> new RuntimeException("Task Not Found"));
        task.setTaskStatus(status);
        return taskRepository.save(task);
    }
}
