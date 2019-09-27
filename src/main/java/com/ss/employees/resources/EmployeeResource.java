package com.ss.employees.resources;

import com.ss.employees.exceptions.ResourceNotFoundException;
import com.ss.employees.model.Employee;
import com.ss.employees.repositories.EmployeeRepository;
import com.ss.employees.representations.EmployeeRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/employees")
public class EmployeeResource {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeResource(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public Page<EmployeeRepresentation> list(
            OAuth2Authentication oAuth2Authentication,
            @RequestParam int page,
            @RequestParam int size) {
        return employeeRepository
                .findAllByUser(oAuth2Authentication.getName(), PageRequest.of(page, size))
                .map(employee -> EmployeeRepresentation.builder().id(employee.getId()).name(employee.getName()).build());
    }

    @PostMapping
    public EmployeeRepresentation create(
            OAuth2Authentication oAuth2Authentication,
            @RequestBody Employee employee) {
        employee.setUser(oAuth2Authentication.getName());
        Employee created = employeeRepository.save(employee);
        return EmployeeRepresentation.builder().id(created.getId()).name(created.getName()).build();
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeRepresentation update(
            OAuth2Authentication oAuth2Authentication,
            @PathVariable Integer id,
            @RequestBody EmployeeRepresentation employeeRepresentation) {
        return employeeRepository
            .findByIdAndUser(id, oAuth2Authentication.getName())
            .map(employee -> employeeRepository.save(employee.update(employeeRepresentation)))
            .map(employee -> EmployeeRepresentation.builder().name(employee.getName()).id(employee.getId()).build())
            .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %s not found", id)));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            OAuth2Authentication oAuth2Authentication,
            @PathVariable Integer id) {
        employeeRepository
            .delete(employeeRepository
                .findByIdAndUser(id, oAuth2Authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %s not found", id))));
    }


}
