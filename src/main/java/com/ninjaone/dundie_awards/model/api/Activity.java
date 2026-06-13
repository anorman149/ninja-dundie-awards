package com.ninjaone.dundie_awards.model.api;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Activity {
    private UUID id;
    private LocalDateTime occurredAt;
    private String event;
}
