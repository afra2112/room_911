package com.room_911.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "employees")
@Data
public class Employee {

    @Id
    @Column(name = "employee_id")
    private String employeeId;

    private String name;

    private String surname;

    private Long document;

    private boolean haveAccess;

    private boolean active;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private ProductionDepartment productionDepartment;

    public Long getDocument() {
        return document;
    }

    public void setDocument(Long document) {
        this.document = document;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public boolean isHaveAccess() {
        return haveAccess;
    }

    public void setHaveAccess(boolean haveAccess) {
        this.haveAccess = haveAccess;
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
