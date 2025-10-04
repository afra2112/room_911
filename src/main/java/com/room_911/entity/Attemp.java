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

    private ResultEnum resultEnum;

    private Long entryNumber;

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

    public ResultEnum getResultEnum() {
        return resultEnum;
    }

    public void setResultEnum(ResultEnum resultEnum) {
        this.resultEnum = resultEnum;
    }

    public Long getEntryNumber() {
        return entryNumber;
    }

    public void setEntryNumber(Long entryNumber) {
        this.entryNumber = entryNumber;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
