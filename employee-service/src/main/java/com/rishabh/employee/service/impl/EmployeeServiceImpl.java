package com.rishabh.employee.service.impl;

import com.rishabh.employee.DTO.requestDto.EmployeeRequestDto;
import com.rishabh.employee.DTO.responseDto.*;
import com.rishabh.employee.client.AddressClient;
import com.rishabh.employee.exception.DuplicateResourceException;
import com.rishabh.employee.exception.ResourceNotFoundException;
import com.rishabh.employee.mapper.EmployeeMapper;
import com.rishabh.employee.model.entity.Employee;
import com.rishabh.employee.repository.EmployeeRepository;
import com.rishabh.employee.service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final AddressClient addressClient;


    @Override
    public EmployeeResponseDto createEmployee(EmployeeRequestDto request) {
        Employee entity = employeeMapper.toEntity(request);
        Employee save = employeeRepository.save(entity);
        return employeeMapper.toResponseDto(save);
    }

    @Override
    @Transactional
    public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto dto) {

        // 1) Null-safety on input
        if (id == null) {
            throw new IllegalArgumentException("Employee id must not be null");
        }
        if (dto == null) {
            throw new IllegalArgumentException("Request body must not be null");
        }

        // 2) Fetch existing employee
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee does not exist with this Id: " + id));

        // 3) Validate & update empEmail (if provided)
        if (dto.getEmpEmail() != null && !dto.getEmpEmail().isBlank()) {
            String newEmail = dto.getEmpEmail().trim();

            // Only check uniqueness if email actually changed
            if (!newEmail.equalsIgnoreCase(employee.getEmpEmail())) {
                if (employeeRepository.existsByEmpEmailAndIdNot(newEmail, id)) {
                    throw new DuplicateResourceException(
                            "Employee with email already exists: " + newEmail);
                }
                employee.setEmpEmail(newEmail);
            }
        }

        // 4) Validate & update empCode (if provided)
        if (dto.getEmpCode() != null && !dto.getEmpCode().isBlank()) {
            String newCode = dto.getEmpCode().trim();

            if (!newCode.equalsIgnoreCase(employee.getEmpCode())) {
                if (employeeRepository.existsByEmpCodeAndIdNot(newCode, id)) {
                    throw new DuplicateResourceException(
                            "Employee with code already exists: " + newCode);
                }
                employee.setEmpCode(newCode);
            }
        }

        // 5) Validate & update empName (if provided)
        if (dto.getEmpName() != null && !dto.getEmpName().isBlank()) {
            employee.setEmpName(dto.getEmpName().trim());
        }

        // 6) Validate & update companyName (if provided)
        if (dto.getCompanyName() != null && !dto.getCompanyName().isBlank()) {
            employee.setCompanyName(dto.getCompanyName().trim());
        }

        // 7) Save changes
        Employee updated = employeeRepository.save(employee);

        // 8) Return mapped response DTO
        return employeeMapper.toResponseDto(updated);
    }

    @Override
    public EmployeeDetailsResponseDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.
                findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("Employee Not Found By Id" + id));

        List<AddressResponseDto> addresses = Collections.emptyList();
        try{

            EmployeeAddressesResponseDto byEmployee =
                    addressClient.getByEmployee(employee.getId());
            if(byEmployee != null && byEmployee.getAddresses() != null){
                  addresses = byEmployee.getAddresses();
            }
        } catch (Exception e) {
            log.error("Address Not Found with employee id " +employee.getId());
        }

        EmployeeDetailsResponseDto dto = employeeMapper.toDetailsDto(employee);
        dto.setAddresses(addresses);
        return dto;

    }

    @Override
    public PageResponseDto<EmployeeResponseDto> getAllEmployee(Pageable pageable) {
        if (pageable.getSort().isUnsorted()){
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("name").ascending()
            );

        }
        Page<Employee> all = employeeRepository.findAll(pageable);
        return PageResponseDto.from(all , employeeMapper::toResponseDto);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.
                findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("Employee Not Found By Id" + id));

        employeeRepository.deleteById(employee.getId());
    }
}
