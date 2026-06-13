package com.ninjaone.dundie_awards.model.mapper;

import com.ninjaone.dundie_awards.model.api.Employee;
import com.ninjaone.dundie_awards.model.entity.EmployeeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper extends EntityMapper<Employee, EmployeeEntity> {

}
