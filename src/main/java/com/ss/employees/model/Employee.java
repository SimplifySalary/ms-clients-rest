package com.ss.employees.model;

import com.ss.employees.representations.EmployeeRepresentation;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Entity
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Employer employer;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Contract> contracts;

    @NotNull
    private String name;

    @NotNull
    private String secondName;

    @NotNull
    private String lastName;

    @NotNull
    private Long documentNumber;

    @NotNull
    private Long cuil;

    @NotNull
    private String user;

    public Employee update(EmployeeRepresentation employeeRepresentation) {
        this.setName(Optional.ofNullable(employeeRepresentation.getName()).orElseGet(this::getName));
        return this;
    }
}
