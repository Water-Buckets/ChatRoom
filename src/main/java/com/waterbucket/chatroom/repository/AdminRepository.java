package com.waterbucket.chatroom.repository;

import com.waterbucket.chatroom.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
    Optional<Admin> findAdminByUsername(String username);
}
