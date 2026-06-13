package com.ninjaone.dundie_awards.model.mapper;

import com.ninjaone.dundie_awards.model.api.Activity;
import com.ninjaone.dundie_awards.model.entity.ActivityEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityMapper extends EntityMapper<Activity, ActivityEntity> {

}
