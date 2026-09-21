package com.rishabh.employee.DTO.responseDto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeAddressesResponseDto {

    private Long empId;
    private List<AddressResponseDto> addresses;

}
