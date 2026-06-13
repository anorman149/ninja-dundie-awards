package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.model.api.Employee;
import com.ninjaone.dundie_awards.model.entity.EmployeeEntity;
import com.ninjaone.dundie_awards.model.mapper.EmployeeMapper;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import io.micrometer.core.annotation.Timed;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(@NonNull EmployeeRepository employeeRepository,
                           @NonNull EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Transactional(readOnly = true)
    @Timed(value = "dundie.employee.service.find.all", histogram = true)
    public Page<Employee> findAll(@NonNull Pageable pageable) {
        Page<EmployeeEntity> page = employeeRepository.findAll(pageable);
        return page.map(employeeMapper::toDto);
    }

    @Cacheable(value = "employees", key = "#id", unless = "#result == null")
    @Transactional(readOnly = true)
    @Timed(value = "dundie.employee.service.find.id", histogram = true)
    public Employee findById(@NonNull UUID id) {
        EmployeeEntity employee = employeeRepository.findById(id).orElse(null);
        return employeeMapper.toDto(employee);
    }

    @Transactional
    @Timed(value = "dundie.employee.service.create", histogram = true)
    public Employee create(@NonNull Employee employee) {
        EmployeeEntity employeeEntity = employeeMapper.toEntity(employee);
        EmployeeEntity savedEmployee = employeeRepository.save(employeeEntity);
        return employeeMapper.toDto(savedEmployee);
    }

    @Caching(evict = { @CacheEvict(value = "employees", key = "#employee.id") })
    @Transactional
    @Timed(value = "dundie.employee.service.update", histogram = true)
    public Employee update(@NonNull Employee employee) {
        EmployeeEntity employeeEntity = employeeMapper.toEntity(employee);
        employeeEntity.setId(employee.getId());
        EmployeeEntity savedEmployee = employeeRepository.save(employeeEntity);
        return employeeMapper.toDto(savedEmployee);
    }

    @Caching(evict = { @CacheEvict(value = "employees", key = "#id") })
    @Transactional
    @Timed(value = "dundie.employee.service.delete", histogram = true)
    public void delete(@NonNull UUID id) {
        employeeRepository.deleteById(id);
    }
}
