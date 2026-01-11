    package com.sanchitp.dev.task.management.system.user.controller;

    import com.sanchitp.dev.task.management.system.user.dto.CreateUserRequest;
    import com.sanchitp.dev.task.management.system.user.dto.UpdateUserRequest;
    import com.sanchitp.dev.task.management.system.user.entity.User;
    import com.sanchitp.dev.task.management.system.user.service.UserService;
    import jakarta.validation.Valid;
    import org.springframework.http.HttpStatus;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/users")
    public class UserController{

        private final UserService userService;

        public UserController(UserService userService) {
            this.userService = userService;
        }

        //Create User
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public User createUser(@Valid @RequestBody CreateUserRequest request){
            return userService.createUser(
                    request.getName(),
                    request.getEmail(),
                    request.getRole()
            );
        }

        //Get User By I'd
        @GetMapping("/{id}")
        public User getUserById(@PathVariable Long id){

            return userService.getUserById(id);
        }

        //Get All Users
        @GetMapping
        public List<User> getAllUsers(){

            return userService.getAllUsers();
        }

        //Delete User By I'd
        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void deleteUser(@PathVariable Long id){

            userService.deleteUser(id);
        }

        //Patch To Update User
        @PatchMapping("/{id}")
        public User updateUser(@PathVariable Long id,@Valid @RequestBody UpdateUserRequest request){
            return userService.updateUser(
                    id,
                    request.getName(),
                    request.getEmail(),
                    request.getRole()
            );
        }

        //Put To Replace User
        @PutMapping("/{id}")
        public User replaceUser(@PathVariable Long id,@Valid @RequestBody CreateUserRequest request){
            return userService.replaceUser(
                    id,
                    request.getName(),
                    request.getEmail(),
                    request.getRole()
            );
        }
    }
