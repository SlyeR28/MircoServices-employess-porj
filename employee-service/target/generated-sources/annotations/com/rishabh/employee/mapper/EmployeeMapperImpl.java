package com.rishabh.employee.mapper;

import com.rishabh.employee.DTO.requestDto.EmployeeRequestDto;
import com.rishabh.employee.DTO.responseDto.EmployeeDetailsResponseDto;
import com.rishabh.employee.DTO.responseDto.EmployeeResponseDto;
import com.rishabh.employee.model.entity.Employee;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-18T10:42:58+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public Employee toEntity(EmployeeRequestDto request) {
        if ( request == null ) {
            return null;
        }

        Employee employee = new Employee();

        employee.setEmpName( request.getEmpName() );
        employee.setEmpEmail( request.getEmpEmail() );
        employee.setEmpCode( request.getEmpCode() );
        employee.setCompanyName( request.getCompanyName() );

        return employee;
    }

    @Override
    public EmployeeResponseDto toResponseDto(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        EmployeeResponseDto.EmployeeResponseDtoBuilder employeeResponseDto = EmployeeResponseDto.builder();

        employeeResponseDto.id( employee.getId() );
        employeeResponseDto.empName( employee.getEmpName() );
        employeeResponseDto.empEmail( employee.getEmpEmail() );
        employeeResponseDto.empCode( employee.getEmpCode() );
        employeeResponseDto.companyName( employee.getCompanyName() );

        return employeeResponseDto.build();
    }

    @Override
    public EmployeeDetailsResponseDto toDetailsDto(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        EmployeeDetailsResponseDto.EmployeeDetailsResponseDtoBuilder employeeDetailsResponseDto = EmployeeDetailsResponseDto.builder();

        employeeDetailsResponseDto.id( employee.getId() );
        employeeDetailsResponseDto.empName( employee.getEmpName() );
        employeeDetailsResponseDto.empCode( employee.getEmpCode() );
        employeeDetailsResponseDto.companyName( employee.getCompanyName() );

        return employeeDetailsResponseDto.build();
    }
}
