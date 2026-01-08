package com.sanchitp.dev.task.management.system.user.repository;

import com.sanchitp.dev.task.management.system.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
