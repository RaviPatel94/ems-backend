package com.ems.employeemanagement.service;

import com.ems.employeemanagement.model.*;
import com.ems.employeemanagement.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository empRepo;
    private final LeaveRepository leaveRepo;

    public EmployeeService(EmployeeRepository empRepo, LeaveRepository leaveRepo) {
        this.empRepo = empRepo;
        this.leaveRepo = leaveRepo;
    }
    
    public Employee getEmployee(Long empId) {
        if (!empRepo.existsById(empId)) {
            throw new RuntimeException("Employee not found with id: " + empId);
        }
        return empRepo.findById(empId).get();
    }
    
    public Employee login(String email, String password) {
        Employee emp = empRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email"));
        if (!emp.getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }
        return emp;
    }

    public LeaveRequest requestLeave(LeaveRequest leave, Long empId) {
        Employee emp = empRepo.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + empId));
        leave.setEmployee(emp);
        leave.setStatus(LeaveStatus.PENDING);
        return leaveRepo.save(leave);
    }

    public List<LeaveRequest> getLeaveHistory(Long empId) {
        if (!empRepo.existsById(empId)) {
            throw new RuntimeException("Employee not found with id: " + empId);
        }
        return leaveRepo.findByEmployeeId(empId);
    }
}
