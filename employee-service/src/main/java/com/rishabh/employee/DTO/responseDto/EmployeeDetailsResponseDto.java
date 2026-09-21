package com.rishabh.employee.DTO.responseDto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDetailsResponseDto {

    private Long id;
    private String empName;
    private String empCode;
    private String companyName;


    private List<AddressResponseDto> addresses;

}
