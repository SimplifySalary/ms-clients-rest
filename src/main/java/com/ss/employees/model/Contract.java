package com.ss.employees.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@Builder
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date since;

    @Enumerated(EnumType.STRING)
    private Modality modality;

    @ManyToOne(fetch = FetchType.LAZY)
    private Employer employer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;

}
