package com.ems.employeemanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    
    private LocalDate fromDate;
    private LocalDate toDate;
    
    @NotBlank(message= "reason cant be blank")
    private String reason;

    @Enumerated(EnumType.STRING)
    private LeaveStatus status;
}
