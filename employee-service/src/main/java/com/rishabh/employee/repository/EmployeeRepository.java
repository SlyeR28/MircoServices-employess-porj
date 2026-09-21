package com.rishabh.employee.repository;

import com.rishabh.employee.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository  extends JpaRepository<Employee , Long> {

    Optional<Employee> findByEmpEmail(String empEmail);
    Optional<Employee> findByEmpCode(String empCode);
    boolean existsByEmpEmailAndIdNot(String email, Long id);
    boolean existsByEmpCodeAndIdNot(String empCode, Long id);
}
