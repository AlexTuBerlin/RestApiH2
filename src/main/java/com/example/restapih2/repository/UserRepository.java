package com.example.restapih2.repository;

import java.util.List;

import com.example.restapih2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);
}
