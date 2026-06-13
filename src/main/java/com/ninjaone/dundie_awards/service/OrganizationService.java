package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.model.api.Organization;
import com.ninjaone.dundie_awards.model.entity.OrganizationEntity;
import com.ninjaone.dundie_awards.model.mapper.OrganizationMapper;
import com.ninjaone.dundie_awards.repository.OrganizationRepository;
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
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    public OrganizationService(@NonNull OrganizationRepository organizationRepository,
                               @NonNull OrganizationMapper organizationMapper) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
    }


    @Transactional(readOnly = true)
    @Timed(value = "dundie.organization.service.find.all", histogram = true)
    public Page<Organization> findAll(@NonNull Pageable pageable) {
        Page<OrganizationEntity> page = organizationRepository.findAll(pageable);
        return page.map(organizationMapper::toDto);
    }

    @Cacheable(value = "organizations", key = "#id", unless = "#result == null")
    @Transactional(readOnly = true)
    @Timed(value = "dundie.organization.service.find.id", histogram = true)
    public Organization findById(@NonNull UUID id) {
        OrganizationEntity a = organizationRepository.findById(id).orElse(null);
        return organizationMapper.toDto(a);
    }

    @Transactional
    @Timed(value = "dundie.organization.service.create", histogram = true)
    public Organization create(@NonNull Organization organization) {
        OrganizationEntity entity = organizationMapper.toEntity(organization);
        OrganizationEntity saved = organizationRepository.save(entity);
        return organizationMapper.toDto(saved);
    }

    @Caching(evict = { @CacheEvict(value = "organizations", key = "#organization.id") })
    @Transactional
    @Timed(value = "dundie.organization.service.update", histogram = true)
    public Organization update(@NonNull Organization organization) {
        OrganizationEntity entity = organizationMapper.toEntity(organization);
        entity.setId(organization.getId());
        OrganizationEntity saved = organizationRepository.save(entity);
        return organizationMapper.toDto(saved);
    }

    @Caching(evict = { @CacheEvict(value = "organizations", key = "#id") })
    @Transactional
    @Timed(value = "dundie.organization.service.delete", histogram = true)
    public void delete(@NonNull UUID id) {
        organizationRepository.deleteById(id);
    }
}
