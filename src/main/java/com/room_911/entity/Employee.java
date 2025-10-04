package com.room_911.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    private String name;

    private String surname;

    private String haveAccess;

    private boolean active;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private ProductionDepartment productionDepartment;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getHaveAccess() {
        return haveAccess;
    }

    public void setHaveAccess(String haveAccess) {
        this.haveAccess = haveAccess;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ProductionDepartment getProductionDepartment() {
        return productionDepartment;
    }

    public void setProductionDepartment(ProductionDepartment productionDepartment) {
        this.productionDepartment = productionDepartment;
    }
}
