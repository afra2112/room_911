package com.room_911.entity;

import com.room_911.config.enums.ResultEnum;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class Attemp{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int attempId;

    private LocalDateTime date;

    private ResultEnum result;

    private String entryNumber;

    private String details;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public int getAttempId() {
        return attempId;
    }

    public void setAttempId(int attempId) {
        this.attempId = attempId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ResultEnum getResult() {
        return result;
    }

    public void setResult(ResultEnum result) {
        this.result = result;
    }

    public String getEntryNumber() {
        return entryNumber;
    }

    public void setEntryNumber(String entryNumber) {
        this.entryNumber = entryNumber;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
