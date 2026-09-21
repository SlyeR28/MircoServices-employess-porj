package com.rishabh.employee.service;

import com.rishabh.employee.DTO.requestDto.EmployeeRequestDto;
import com.rishabh.employee.DTO.responseDto.EmployeeDetailsResponseDto;
import com.rishabh.employee.DTO.responseDto.EmployeeResponseDto;
import com.rishabh.employee.DTO.responseDto.PageResponseDto;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    EmployeeResponseDto createEmployee(EmployeeRequestDto request);

    EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto dto);

    EmployeeDetailsResponseDto getEmployeeById(Long id);

    void deleteEmployee(Long id);

    PageResponseDto<EmployeeResponseDto> getAllEmployee(Pageable pageable);

}
