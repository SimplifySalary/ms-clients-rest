package com.ss.employees.repositories;

import com.ss.employees.model.Employee;
import com.ss.employees.model.Employer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Page<Employee> findAllByEmployerAndUser(Employer employer, String user, Pageable pageable);

    Page<Employee> findAllByUser(String name, Pageable pageable);

    Optional<Employee> findByIdAndUser(Integer employeeId, String user);

    Optional<Employee> findByIdAndUserAndEmployer(Integer employeeId, String name, Employer savedEmployer);
}
