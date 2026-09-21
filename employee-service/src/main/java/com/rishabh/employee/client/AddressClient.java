package com.rishabh.employee.client;

import com.rishabh.employee.DTO.responseDto.EmployeeAddressesResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ADDRESS-SERVICE" , url = "${address.service.url}")
public interface AddressClient {

    @GetMapping("/employee/{empId}")
    EmployeeAddressesResponseDto getByEmployee(@PathVariable Long empId);
}
