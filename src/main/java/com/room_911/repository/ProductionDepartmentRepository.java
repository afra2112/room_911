package com.room_911.repository;

import com.room_911.entity.ProductionDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionDepartmentRepository extends JpaRepository<ProductionDepartment, Long> {
    ProductionDepartment findByName(String name);
}
