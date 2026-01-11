package com.sanchitp.dev.task.management.system.user.service;

import com.sanchitp.dev.task.management.system.common.enums.Role;
import com.sanchitp.dev.task.management.system.task.entity.Task;
import com.sanchitp.dev.task.management.system.task.repository.TaskRepository;
import com.sanchitp.dev.task.management.system.user.entity.User;
import com.sanchitp.dev.task.management.system.user.exception.UserNotFoundException;
import com.sanchitp.dev.task.management.system.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;


    public UserService(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    //Service For Create User
    public User createUser(String name, String email, Role role){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setRole(role);
        return userRepository.save(user);
    }

    //Service To Get User By I'd
    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    //Service To Get All Users
    public List<User> getAllUsers(){

        return userRepository.findAll();
    }

    //Service To Delete User By I'd
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        List<Task> tasks = taskRepository.findByAssignedTo(user);
        for (Task task : tasks) {
            task.setAssignedTo(null);
        }
        userRepository.delete(user);
    }

    //Service Replace User By I'd
    public User replaceUser(Long id,String name,String email,Role role){
        User user = getUserById(id);

        user.setName(name);
        user.setEmail(email);
        user.setRole(role);

        return userRepository.save(user);
    }

    //Service To Update User
    public  User updateUser(Long id,String name,String email,Role role){
        User user = getUserById(id);

        if (name != null){
            user.setName(name);
        }
        if (email != null){
            user.setEmail(email);
        }
        if (role != null){
            user.setRole(role);
        }

        return userRepository.save(user);
    }


}
