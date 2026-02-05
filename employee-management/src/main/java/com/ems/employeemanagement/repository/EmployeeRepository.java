package com.ems.employeemanagement.repository;

import com.ems.employeemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Employee> findByHrId(Long hrId);
}
