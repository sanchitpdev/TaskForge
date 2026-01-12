package com.sanchitp.dev.task.management.system.user.service;

import com.sanchitp.dev.task.management.system.common.enums.Role;
import com.sanchitp.dev.task.management.system.common.enums.exception.UserNotFoundException;
import com.sanchitp.dev.task.management.system.task.entity.Task;
import com.sanchitp.dev.task.management.system.task.repository.TaskRepository;
import com.sanchitp.dev.task.management.system.user.dto.UserResponse;
import com.sanchitp.dev.task.management.system.user.entity.User;
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

    /* ===================== HELPERS ===================== */

    private UserResponse toResponse(User user){
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    private User getUserEntityById(Long id){
        return userRepository.findById(id).orElseThrow(()->new UserNotFoundException(id));
    }

    /* ===================== CRUD ===================== */

    //Service For Create User
    public UserResponse createUser(String name, String email, Role role){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setRole(role);
        return toResponse(userRepository.save(user));
    }

    //Service To Get User By I'd
    public UserResponse getUserById(Long id){
        return toResponse(getUserEntityById(id));

    }

    //Service To Get All Users
    public List<UserResponse> getAllUsers(){

        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    //Service To Delete User By I'd
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserEntityById(id);
        List<Task> tasks = taskRepository.findByAssignedTo(user);
        for (Task task : tasks) {
            task.setAssignedTo(null);
        }
        userRepository.delete(user);
    }

    //Service Replace User By I'd
    public UserResponse replaceUser(Long id,String name,String email,Role role){
        User user = getUserEntityById(id);

        user.setName(name);
        user.setEmail(email);
        user.setRole(role);

        return toResponse(userRepository.save(user));
    }

    //Service To Update User
    public UserResponse updateUser(Long id, String name, String email, Role role) {
        // internal helper if you want;
        User user = getUserEntityById(id);

        if (name != null) user.setName(name);
        if (email != null) user.setEmail(email);
        if (role != null) user.setRole(role);
        return toResponse(userRepository.save(user));
    }









}
