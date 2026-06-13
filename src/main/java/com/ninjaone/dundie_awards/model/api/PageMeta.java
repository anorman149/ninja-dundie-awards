package com.ninjaone.dundie_awards.model.api;

public record PageMeta(
        int number,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious) {}
