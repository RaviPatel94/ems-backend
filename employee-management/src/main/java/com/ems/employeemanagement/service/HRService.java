package com.ems.employeemanagement.service;

import com.ems.employeemanagement.model.*;
import com.ems.employeemanagement.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HRService {

    private final HRRepository hrRepo;
    private final EmployeeRepository empRepo;
    private final LeaveRepository leaveRepo;

    public HRService(HRRepository hrRepo, EmployeeRepository empRepo, LeaveRepository leaveRepo) {
        this.hrRepo = hrRepo;
        this.empRepo = empRepo;
        this.leaveRepo = leaveRepo;
    }

    public HR signup(HR hr) {
        if (hrRepo.existsByEmail(hr.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        hr.setStatus(EmployeeStatus.AVAILABLE);
        return hrRepo.save(hr);
    }

    public HR login(String email, String password) {
        HR hr = hrRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email doesn't exist"));
        if (!hr.getPassword().equals(password)) {
            throw new RuntimeException("Incorrect Password");
        }
        return hr;
    }

    public Employee addEmployee(Employee employee, Long hrId) {
        if (empRepo.existsByEmail(employee.getEmail())) {
            throw new RuntimeException("Employee email already exists");
        }

        HR hr = hrRepo.findById(hrId)
                .orElseThrow(() -> new RuntimeException("HR not found with id: " + hrId));

        employee.setStatus(EmployeeStatus.AVAILABLE);
        employee.setHr(hr);
        return empRepo.save(employee);
    }

    public Employee changeEmployeeStatus(Long empId, EmployeeStatus status) {
        Employee emp = empRepo.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + empId));
        emp.setStatus(status);
        return empRepo.save(emp);
    }

    public List<Employee> getEmployeesByHR(Long hrId) {
        if (!hrRepo.existsById(hrId)) {
            throw new RuntimeException("HR not found with id: " + hrId);
        }
        List<Employee> employees = empRepo.findByHrId(hrId);
        if (employees.isEmpty()) {
            throw new RuntimeException("No employees found for HR id: " + hrId);
        }
        return employees;
    }

    public LeaveRequest approveLeave(Long leaveId) {
        LeaveRequest lr = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + leaveId));
        lr.setStatus(LeaveStatus.APPROVED);
        return leaveRepo.save(lr);
    }

    public LeaveRequest rejectLeave(Long leaveId) {
        LeaveRequest lr = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + leaveId));
        lr.setStatus(LeaveStatus.REJECTED);
        return leaveRepo.save(lr);
    }

    public List<LeaveRequest> getAllLeaves() {
        List<LeaveRequest> leaves = leaveRepo.findAll();
        return leaves;
    }
}
