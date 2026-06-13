package com.ninjaone.dundie_awards.model.api;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Organization {
    private UUID id;
    private String name;
}
