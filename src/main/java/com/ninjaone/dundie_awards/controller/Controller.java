package com.ninjaone.dundie_awards.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

abstract class Controller {
    protected static final int DEFAULT_PAGE_SIZE = 25;
    protected static final int MAX_PAGE_SIZE = 500;

    protected Pageable validatePageable(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();

        if(pageNumber < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page number must be greater than or equal to 0");
        }
        if(pageSize <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page size must be greater than 0");
        }
        if(pageSize > MAX_PAGE_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page size must be less than or equal to " + MAX_PAGE_SIZE);
        }

        return PageRequest.of(pageNumber, pageSize, pageable.getSort());
    }
}
