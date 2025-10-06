package com.room_911.repository;

import com.room_911.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByUsernameAndActive(String username, boolean active);
    List<Admin> findByActive(boolean active);
}
