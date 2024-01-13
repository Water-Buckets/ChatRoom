package com.waterbucket.chatroom.repository;

import com.waterbucket.chatroom.model.Admin;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminRepository extends R2dbcRepository<Admin, UUID> {
    Optional<Admin> findAdminByUsername(String username);
}
