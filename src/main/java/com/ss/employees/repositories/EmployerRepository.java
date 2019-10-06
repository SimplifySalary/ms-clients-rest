package com.ss.employees.repositories;

import com.ss.employees.model.Employer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployerRepository extends JpaRepository<Employer, Integer> {

    Page<Employer> findAllByUser(String user, Pageable pageable);

    Optional<Employer> findByIdAndUser(Integer id, String user);

}
