package com.example.chatClips.repository;

import com.example.chatClips.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    default Optional<User> findByUsername(String username) {
        return findAll().stream()
                .filter(member -> member.getUsername().equals(username))
                .findFirst();
    }
}
