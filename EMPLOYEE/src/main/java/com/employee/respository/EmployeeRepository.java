package com.employee.respository;

import com.employee.model.entity.Employee;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmpCodeAndCompanyName(String empCode, String companyName);
}
