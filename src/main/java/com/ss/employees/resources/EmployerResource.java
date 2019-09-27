package com.ss.employees.resources;

import com.ss.employees.model.Employee;
import com.ss.employees.model.Employer;
import com.ss.employees.repositories.EmployeeRepository;
import com.ss.employees.repositories.EmployerRepository;
import com.ss.employees.representations.EmployeeRepresentation;
import com.ss.employees.representations.EmployerRepresentation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/api/employers")
public class EmployerResource {

    private final EmployerRepository employerRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployerResource(EmployerRepository employerRepository, EmployeeRepository employeeRepository) {
        this.employerRepository = employerRepository;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public Page<EmployerRepresentation> list(
            OAuth2Authentication auth2Authentication,
            @RequestParam int page,
            @RequestParam int size) {
        return employerRepository
            .findAllByUser(auth2Authentication.getName(), PageRequest.of(page, size))
            .map(employer ->
                EmployerRepresentation.builder().id(employer.getId()).description(employer.getDescription()).build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployerRepresentation create(OAuth2Authentication principal, @RequestBody Employer employer) {
        employer.setUser(principal.getName());
        Employer created = employerRepository.save(employer);
        return EmployerRepresentation.builder().id(created.getId()).description(created.getDescription()).build();
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployerRepresentation update(
            @PathVariable Integer id,
            OAuth2Authentication oAuth2Authentication,
            @RequestBody EmployerRepresentation employerRepresentation) {
        return this.employerRepository
            .findByIdAndUser(id, oAuth2Authentication.getName())
            .map(employer -> employerRepository.save(employer.update(employerRepresentation)))
            .map(employer -> EmployerRepresentation.builder().id(employer.getId()).description(employer.getDescription()).build())
            .orElseThrow(() -> new ResourceAccessException(String.format("Employer with id %s not found", id)));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Integer id,
            OAuth2Authentication oAuth2Authentication) {
        this.employerRepository.delete(
            this.employerRepository
                .findByIdAndUser(id, oAuth2Authentication.getName())
                .orElseThrow(() -> new ResourceAccessException(String.format("Employer with id %s not found", id))));
    }


    @GetMapping(value = "/{id}/employees")
    @ResponseStatus(HttpStatus.OK)
    public Page<EmployeeRepresentation> listEmployees(
            OAuth2Authentication auth2Authentication,
            @PathVariable Integer id,
            @RequestParam int page,
            @RequestParam int size) {

        Employer employer = employerRepository
            .findByIdAndUser(id, auth2Authentication.getName())
            .orElseThrow(() -> new ResourceAccessException(String.format("Employer with id %s not found", id)));

        return employeeRepository
            .findAllByEmployerAndUser(employer, auth2Authentication.getName(), PageRequest.of(page, size))
            .map(employee -> EmployeeRepresentation.builder().id(employee.getId()).name(employee.getName()).build());
    }

    @PutMapping(value = "/{id}/employees")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addEmployee(
            OAuth2Authentication oAuth2Authentication,
            @PathVariable Integer id,
            @RequestBody Employee employee) {

        employee.setUser(oAuth2Authentication.getName());

        Optional<Employee> savedEmployee =
        Optional
            .ofNullable(employee.getId())
            .map(employeeId ->
                employeeRepository
                    .findByIdAndUser(employeeId, oAuth2Authentication.getName())
                    .orElseThrow(() -> new ResourceAccessException(String.format("Employee with id %s not found", employeeId))));

        employerRepository
            .findByIdAndUser(id, oAuth2Authentication.getName())
            .map(employer -> employerRepository.save(employer.addEmployee(savedEmployee.orElse(employee))))
            .orElseThrow(() -> new ResourceAccessException(String.format("Employer with id %s not found", id)));
    }

    @DeleteMapping(value = "/{id}/employees/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeEmployee(
            OAuth2Authentication oAuth2Authentication,
            @PathVariable Integer id,
            @PathVariable Integer employeeId) {

        Employer savedEmployer = employerRepository
            .findByIdAndUser(id, oAuth2Authentication.getName())
            .orElseThrow(() -> new ResourceAccessException(String.format("Employer with id %s not found", id)));

        Employee savedEmployee = employeeRepository
                .findByIdAndUserAndEmployer(employeeId, oAuth2Authentication.getName(), savedEmployer)
                .orElseThrow(() -> new ResourceAccessException(String.format("Employee with id %s not found", employeeId)));

        employerRepository.save(savedEmployer.removeEmployee(savedEmployee));

    }

}
