package com.ninjaone.dundie_awards.model.api;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Employee {
    private UUID id;
    private String firstName;
    private String lastName;
    private Integer dundieAwards;
    private Organization organization;
}