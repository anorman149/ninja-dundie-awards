package com.ninjaone.dundie_awards.controller;

import com.ninjaone.dundie_awards.model.api.Employee;
import com.ninjaone.dundie_awards.model.api.PagedResponse;
import com.ninjaone.dundie_awards.service.EmployeeService;
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
@RequestMapping("/employees")
public class EmployeeController extends Controller {
    private final EmployeeService employeeService;

    public EmployeeController(@NonNull EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // get all employees
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "dundie.employee.find.all", histogram = true)
    public ResponseEntity<PagedResponse<Employee>> findAll(@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        //Validate Page
        Pageable validatedPageable = validatePageable(pageable);

        //Perform Service Call
        Page<Employee> paged = employeeService.findAll(validatedPageable);

        //Transform and return
        return ResponseEntity.ok().body(PagedResponse.from(paged));
    }

    // get employee by id rest api
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "dundie.employee.find.id", histogram = true)
    public ResponseEntity<Employee> findById(@PathVariable UUID id) {
        Employee employee = employeeService.findById(id);
        if (employee == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(employee);
    }

    // create employee rest api
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "dundie.employee.create", histogram = true)
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        Employee e = employeeService.create(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(e);
    }

    // update employee rest api
    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "dundie.employee.update", histogram = true)
    public ResponseEntity<Employee> update(@RequestBody Employee employee) {
        Employee e = employeeService.update(employee);
        return ResponseEntity.ok().body(e);
    }

    // delete employee rest api
    @DeleteMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "dundie.employee.delete", histogram = true)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        employeeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
