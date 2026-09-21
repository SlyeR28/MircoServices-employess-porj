package com.rishabh.employee.controller;

import com.rishabh.employee.DTO.requestDto.EmployeeRequestDto;
import com.rishabh.employee.DTO.responseDto.EmployeeDetailsResponseDto;
import com.rishabh.employee.DTO.responseDto.EmployeeResponseDto;
import com.rishabh.employee.DTO.responseDto.PageResponseDto;
import com.rishabh.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;


    @PostMapping("/create")
    public ResponseEntity<EmployeeResponseDto> createEmployee(@Valid @RequestBody EmployeeRequestDto request){
        EmployeeResponseDto employee = employeeService.createEmployee(request);
        return ResponseEntity.ok(employee);
     }


     @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee( @PathVariable Long id , @RequestBody EmployeeRequestDto request) {
        EmployeeResponseDto employee = employeeService.updateEmployee(id ,request);
        return ResponseEntity.ok(employee);
     }

     @GetMapping("/{id}")
    public ResponseEntity<EmployeeDetailsResponseDto>getEmployeeById(@PathVariable Long id){
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
     }

     @GetMapping("/")
    public ResponseEntity<PageResponseDto<EmployeeResponseDto>>getAllEmployee(
             @RequestParam(defaultValue = "0")    int page,
             @RequestParam(defaultValue = "10")   int size,
             @RequestParam(defaultValue = "empName") String sortBy,
             @RequestParam(defaultValue = "asc")  String direction) {

         Sort sort = direction.equalsIgnoreCase("desc")
                 ? Sort.by(sortBy).descending()
                 : Sort.by(sortBy).ascending();

         Pageable pageable = PageRequest.of(page, size, sort);

         return ResponseEntity.ok(employeeService.getAllEmployee(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Deleted");
    }

    }
