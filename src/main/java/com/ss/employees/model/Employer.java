package com.ss.employees.model;

import com.ss.employees.representations.EmployerRepresentation;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Entity(name = "employer")
@Data
public class Employer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String description;

    @NotNull
    private Long cuit;

    @NotNull
    private String address;

    @NotNull
    private String user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "employer")
    private List<Employee> employees;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "employer")
    private List<Contract> contracts;

    public Employer addEmployee(Employee employee) {
        employee.setEmployer(this);
        this.getEmployees().add(employee);
        this.contracts.add(Contract.builder().employee(employee).employer(this).build());
        return this;
    }

    public Employer update(EmployerRepresentation employerRepresentation) {
        this.setDescription(Optional.ofNullable(employerRepresentation.getDescription()).orElse(this.getDescription()));
        return this;
    }

    public Employer removeEmployee(Employee savedEmployee) {
        this.getEmployees().remove(savedEmployee);
        savedEmployee.setEmployer(null);
        return this;
    }
}
