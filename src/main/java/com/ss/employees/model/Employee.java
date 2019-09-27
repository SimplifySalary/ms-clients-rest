package com.ss.employees.model;

import com.ss.employees.representations.EmployeeRepresentation;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Entity
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Employer employer;

    @NotNull
    private String name;

    @NotNull
    private String user;

    public Employee update(EmployeeRepresentation employeeRepresentation) {
        this.setName(Optional.ofNullable(employeeRepresentation.getName()).orElseGet(this::getName));
        return this;
    }
}
