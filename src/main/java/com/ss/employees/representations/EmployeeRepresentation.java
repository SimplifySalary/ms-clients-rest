package com.ss.employees.representations;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeRepresentation {

    private Integer id;

    private String name;
}
