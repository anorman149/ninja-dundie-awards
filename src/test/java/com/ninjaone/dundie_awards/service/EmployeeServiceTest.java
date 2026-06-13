package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.model.api.Employee;
import com.ninjaone.dundie_awards.model.entity.EmployeeEntity;
import com.ninjaone.dundie_awards.model.mapper.EmployeeMapper;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService(employeeRepository, employeeMapper);
    }

    @Test
    void findAll_mapsEntityPageToDtoPage() {
        EmployeeEntity entity = EmployeeEntity.builder().id(UUID.randomUUID()).firstName("Pam").lastName("Beesly").build();
        Employee dto = Employee.builder().id(entity.getId()).firstName("Pam").lastName("Beesly").build();
        Pageable pageable = PageRequest.of(0, 25);
        Page<EmployeeEntity> entityPage = new PageImpl<>(List.of(entity), pageable, 1);

        when(employeeRepository.findAll(pageable)).thenReturn(entityPage);
        when(employeeMapper.toDto(entity)).thenReturn(dto);

        Page<Employee> result = employeeService.findAll(pageable);

        assertThat(result.getContent()).containsExactly(dto);
        verify(employeeRepository).findAll(pageable);
    }

    @Test
    void findById_existingId_returnsMappedEmployee() {
        UUID id = UUID.randomUUID();
        EmployeeEntity entity = EmployeeEntity.builder().id(id).firstName("Jim").lastName("Halpert").build();
        Employee dto = Employee.builder().id(id).firstName("Jim").lastName("Halpert").build();

        when(employeeRepository.findById(id)).thenReturn(Optional.of(entity));
        when(employeeMapper.toDto(entity)).thenReturn(dto);

        Employee result = employeeService.findById(id);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void findById_unknownId_returnsNull() {
        UUID id = UUID.randomUUID();
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        Employee result = employeeService.findById(id);

        assertThat(result).isNull();
    }

    @Test
    void create_savesEntityAndReturnsMappedEmployee() {
        Employee dto = Employee.builder().firstName("Dwight").lastName("Schrute").build();
        EmployeeEntity entityToSave = EmployeeEntity.builder().firstName("Dwight").lastName("Schrute").build();
        EmployeeEntity savedEntity = EmployeeEntity.builder().id(UUID.randomUUID()).firstName("Dwight").lastName("Schrute").build();
        Employee savedDto = Employee.builder().id(savedEntity.getId()).firstName("Dwight").lastName("Schrute").build();

        when(employeeMapper.toEntity(dto)).thenReturn(entityToSave);
        when(employeeRepository.save(entityToSave)).thenReturn(savedEntity);
        when(employeeMapper.toDto(savedEntity)).thenReturn(savedDto);

        Employee result = employeeService.create(dto);

        assertThat(result).isEqualTo(savedDto);
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void update_setsIdOnMappedEntityBeforeSaving() {
        UUID id = UUID.randomUUID();
        Employee dto = Employee.builder().id(id).firstName("Andy").lastName("Bernard").build();
        EmployeeEntity mappedEntity = EmployeeEntity.builder().firstName("Andy").lastName("Bernard").build();
        EmployeeEntity savedEntity = EmployeeEntity.builder().id(id).firstName("Andy").lastName("Bernard").build();
        Employee savedDto = Employee.builder().id(id).firstName("Andy").lastName("Bernard").build();

        when(employeeMapper.toEntity(dto)).thenReturn(mappedEntity);
        when(employeeRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(employeeMapper.toDto(savedEntity)).thenReturn(savedDto);

        Employee result = employeeService.update(dto);

        assertThat(mappedEntity.getId()).isEqualTo(id);
        assertThat(result).isEqualTo(savedDto);
    }

    @Test
    void delete_delegatesToRepository() {
        UUID id = UUID.randomUUID();

        employeeService.delete(id);

        verify(employeeRepository).deleteById(id);
    }
}
