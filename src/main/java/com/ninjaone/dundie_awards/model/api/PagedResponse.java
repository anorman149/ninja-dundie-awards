package com.ninjaone.dundie_awards.model.api;

import org.springframework.data.domain.Page;

import java.util.Collection;

public record PagedResponse<T>(Collection<T> content, PageMeta page) {

    public static <T> PagedResponse<T> from(Page<T> page) {
        return new PagedResponse<>(page.getContent(),
                new PageMeta(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages(),
                        page.hasNext(),
                        page.hasPrevious()));
    }
}
