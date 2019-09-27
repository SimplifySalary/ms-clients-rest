package com.ss.employees.representations;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployerRepresentation {

    private Integer id;

    private String description;
}
