package com.itransitionTasks.itransitionTask_4.repositary;

import com.itransitionTasks.itransitionTask_4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUsername(String username);

    public void deleteUserById(Long id);

    public Optional<User> findById(Long id);

    public User findByEmail(String email);
}
