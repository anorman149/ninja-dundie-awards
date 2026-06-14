package com.ninjaone.dundie_awards.controller;

import com.ninjaone.dundie_awards.model.api.Organization;
import com.ninjaone.dundie_awards.model.api.PagedResponse;
import com.ninjaone.dundie_awards.service.OrganizationService;
import io.micrometer.core.annotation.Timed;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/organizations")
public class OrganizationController extends Controller {
    private final OrganizationService organizationService;

    public OrganizationController(@NonNull OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    // get all organizations
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "dundie.organization.find.all", histogram = true)
    public ResponseEntity<PagedResponse<Organization>> findAll(@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        //Validate Page
        Pageable validatedPageable = validatePageable(pageable);

        //Perform Service Call
        Page<Organization> paged = organizationService.findAll(validatedPageable);

        //Transform and return
        return ResponseEntity.ok().body(PagedResponse.from(paged));
    }

    // get organization by id rest api
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "dundie.organization.find.id", histogram = true)
    public ResponseEntity<Organization> findById(@PathVariable UUID id) {
        Organization o = organizationService.findById(id);
        if (o == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(o);
    }

    // create organization rest api
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "dundie.organization.create", histogram = true)
    public ResponseEntity<Organization> create(@RequestBody Organization organization) {
        Organization o = organizationService.create(organization);
        return ResponseEntity.status(HttpStatus.CREATED).body(o);
    }

    // update organization rest api
    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "dundie.organization.update", histogram = true)
    public ResponseEntity<Organization> update(@RequestBody Organization organization) {
        Organization o = organizationService.update(organization);
        return ResponseEntity.ok().body(o);
    }

    // delete organization rest api
    @DeleteMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "dundie.organization.delete", histogram = true)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        organizationService.delete(id);
        return ResponseEntity.ok().build();
    }
}
