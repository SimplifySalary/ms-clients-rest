package com.ss.employees.representations;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorRepresentation {

    private String message;
}
