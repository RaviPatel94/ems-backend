package com.ems.employeemanagement.controller;

import com.ems.employeemanagement.model.*;
import com.ems.employeemanagement.security.JwtUtil;
import com.ems.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService empService;
    private final JwtUtil jwtUtil;

    public EmployeeController(EmployeeService empService, JwtUtil jwtUtil) {
        this.empService = empService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Employee emp) {
        Employee employee = empService.login(emp.getEmail(), emp.getPassword());
        String token = jwtUtil.generateToken(employee.getEmail(), employee.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", "employee");
        response.put("id",employee.getId());
        response.put("message", "Login successful");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/leave/{empId}")
    public ResponseEntity<Map<String, Object>> requestLeave(
            @RequestBody @Valid LeaveRequest leave,
            @PathVariable Long empId) {

        LeaveRequest savedLeave = empService.requestLeave(leave, empId);
        Map<String, Object> response = new HashMap<>();
        response.put("leaveId", savedLeave.getId());
        response.put("status", savedLeave.getStatus());
        response.put("message", "Leave requested successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/leave/history/{empId}")
    public ResponseEntity<List<LeaveRequest>> history(@PathVariable Long empId) {
        List<LeaveRequest> leaves = empService.getLeaveHistory(empId);
        return ResponseEntity.ok(leaves);
    }
}
