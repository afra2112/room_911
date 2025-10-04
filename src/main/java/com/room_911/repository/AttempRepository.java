package com.room_911.repository;

import com.room_911.entity.Attemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttempRepository extends JpaRepository<Attemp, Long> {
}
