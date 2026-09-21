package com.rishabh.employee.mapper;

import com.rishabh.employee.DTO.requestDto.EmployeeRequestDto;
import com.rishabh.employee.DTO.responseDto.EmployeeDetailsResponseDto;
import com.rishabh.employee.DTO.responseDto.EmployeeResponseDto;
import com.rishabh.employee.model.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee toEntity(EmployeeRequestDto request);

    EmployeeResponseDto toResponseDto(Employee employee);

    @Mapping(target = "addresses" , ignore = true)
    EmployeeDetailsResponseDto  toDetailsDto(Employee employee);

}
