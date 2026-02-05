package com.ems.employeemanagement.controller;

import com.ems.employeemanagement.model.*;
import com.ems.employeemanagement.security.JwtUtil;
import com.ems.employeemanagement.service.HRService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hr")
public class HRController {

    private final HRService hrService;
    private final JwtUtil jwtUtil;

    public HRController(HRService hrService, JwtUtil jwtUtil) {
        this.hrService = hrService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody @Valid HR hr) {
        HR savedHr = hrService.signup(hr);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "HR registered successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody HR hr) {
        HR hrUser = hrService.login(hr.getEmail(), hr.getPassword());
        String token = jwtUtil.generateToken(hrUser.getEmail(), hrUser.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", "hr");
        response.put("id", hrUser.getId());
        response.put("message", "Login successful");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/addEmployee/{hrId}")
    public ResponseEntity<Map<String, Object>> addEmployee(
            @RequestBody @Valid Employee emp,
            @PathVariable Long hrId) {

        Employee addedEmp = hrService.addEmployee(emp, hrId);
        Map<String, Object> response = new HashMap<>();
        response.put("employeeId", addedEmp.getId());
        response.put("message", "Employee added successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/employee/status/{empId}")
    public ResponseEntity<Map<String, Object>> changeStatus(
            @PathVariable Long empId,
            @RequestParam EmployeeStatus status) {

        Employee updatedEmp = hrService.changeEmployeeStatus(empId, status);
        Map<String, Object> response = new HashMap<>();
        response.put("employeeId", updatedEmp.getId());
        response.put("status", updatedEmp.getStatus());
        response.put("message", "Employee status updated");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/employees/{hrId}")
    public ResponseEntity<List<Employee>> getEmployees(@PathVariable Long hrId) {
        List<Employee> employees = hrService.getEmployeesByHR(hrId);
        return ResponseEntity.ok(employees);
    }

    @PutMapping("/leave/approve/{leaveId}")
    public ResponseEntity<Map<String, Object>> approve(@PathVariable Long leaveId) {
        LeaveRequest lr = hrService.approveLeave(leaveId);
        Map<String, Object> response = new HashMap<>();
        response.put("leaveId", lr.getId());
        response.put("status", lr.getStatus());
        response.put("message", "Leave approved");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/leave/reject/{leaveId}")
    public ResponseEntity<Map<String, Object>> reject(@PathVariable Long leaveId) {
        LeaveRequest lr = hrService.rejectLeave(leaveId);
        Map<String, Object> response = new HashMap<>();
        response.put("leaveId", lr.getId());
        response.put("status", lr.getStatus());
        response.put("message", "Leave rejected");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/leaves")
    public ResponseEntity<List<LeaveRequest>> getAllLeaves() {
        List<LeaveRequest> leaves = hrService.getAllLeaves();
        return ResponseEntity.ok(leaves);
    }
}
