package com.ninjaone.dundie_awards.model.mapper;

import com.ninjaone.dundie_awards.model.api.Organization;
import com.ninjaone.dundie_awards.model.entity.OrganizationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationMapper extends EntityMapper<Organization, OrganizationEntity> {

}
