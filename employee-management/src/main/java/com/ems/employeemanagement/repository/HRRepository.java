package com.ems.employeemanagement.repository;

import com.ems.employeemanagement.model.HR;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HRRepository extends JpaRepository<HR, Long> {
    Optional<HR> findByEmail(String email);
    boolean existsByEmail(String email);
}
