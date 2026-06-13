package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.model.api.Organization;
import com.ninjaone.dundie_awards.model.entity.OrganizationEntity;
import com.ninjaone.dundie_awards.model.mapper.OrganizationMapper;
import com.ninjaone.dundie_awards.repository.OrganizationRepository;
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
class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private OrganizationMapper organizationMapper;

    private OrganizationService organizationService;

    @BeforeEach
    void setUp() {
        organizationService = new OrganizationService(organizationRepository, organizationMapper);
    }

    @Test
    void findAll_mapsEntityPageToDtoPage() {
        OrganizationEntity entity = OrganizationEntity.builder().id(UUID.randomUUID()).name("Dunder Mifflin").build();
        Organization dto = Organization.builder().id(entity.getId()).name("Dunder Mifflin").build();
        Pageable pageable = PageRequest.of(0, 25);
        Page<OrganizationEntity> entityPage = new PageImpl<>(List.of(entity), pageable, 1);

        when(organizationRepository.findAll(pageable)).thenReturn(entityPage);
        when(organizationMapper.toDto(entity)).thenReturn(dto);

        Page<Organization> result = organizationService.findAll(pageable);

        assertThat(result.getContent()).containsExactly(dto);
        verify(organizationRepository).findAll(pageable);
    }

    @Test
    void findById_existingId_returnsMappedOrganization() {
        UUID id = UUID.randomUUID();
        OrganizationEntity entity = OrganizationEntity.builder().id(id).name("Pikashu").build();
        Organization dto = Organization.builder().id(id).name("Pikashu").build();

        when(organizationRepository.findById(id)).thenReturn(Optional.of(entity));
        when(organizationMapper.toDto(entity)).thenReturn(dto);

        Organization result = organizationService.findById(id);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void findById_unknownId_returnsNull() {
        UUID id = UUID.randomUUID();
        when(organizationRepository.findById(id)).thenReturn(Optional.empty());

        Organization result = organizationService.findById(id);

        assertThat(result).isNull();
    }

    @Test
    void create_savesEntityAndReturnsMappedOrganization() {
        Organization dto = Organization.builder().name("Squanchy").build();
        OrganizationEntity entityToSave = OrganizationEntity.builder().name("Squanchy").build();
        OrganizationEntity savedEntity = OrganizationEntity.builder().id(UUID.randomUUID()).name("Squanchy").build();
        Organization savedDto = Organization.builder().id(savedEntity.getId()).name("Squanchy").build();

        when(organizationMapper.toEntity(dto)).thenReturn(entityToSave);
        when(organizationRepository.save(entityToSave)).thenReturn(savedEntity);
        when(organizationMapper.toDto(savedEntity)).thenReturn(savedDto);

        Organization result = organizationService.create(dto);

        assertThat(result).isEqualTo(savedDto);
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void update_setsIdOnMappedEntityBeforeSaving() {
        UUID id = UUID.randomUUID();
        Organization dto = Organization.builder().id(id).name("Renamed Org").build();
        OrganizationEntity mappedEntity = OrganizationEntity.builder().name("Renamed Org").build();
        OrganizationEntity savedEntity = OrganizationEntity.builder().id(id).name("Renamed Org").build();
        Organization savedDto = Organization.builder().id(id).name("Renamed Org").build();

        when(organizationMapper.toEntity(dto)).thenReturn(mappedEntity);
        when(organizationRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(organizationMapper.toDto(savedEntity)).thenReturn(savedDto);

        Organization result = organizationService.update(dto);

        assertThat(mappedEntity.getId()).isEqualTo(id);
        assertThat(result).isEqualTo(savedDto);
    }

    @Test
    void delete_delegatesToRepository() {
        UUID id = UUID.randomUUID();

        organizationService.delete(id);

        verify(organizationRepository).deleteById(id);
    }
}
