package com.hexaware.easypay.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.hexaware.easypay.dto.EmployeeDTO;
import com.hexaware.easypay.entity.Employee;
import com.hexaware.easypay.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @PostMapping
    public Employee addEmployee(
            @Valid @RequestBody EmployeeDTO dto) {

        return service.addEmployee(dto);
    }

    @GetMapping("/{id}")
    public Employee getEmployee(
            @PathVariable Long id) {

        return service.getEmployeeById(id);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {

        return service.getAllEmployees();
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO dto) {

        return service.updateEmployee(id, dto);
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(
            @PathVariable Long id) {

        service.deleteEmployee(id);

        return "Employee Deleted Successfully";
    }
    
    @PutMapping("/{id}/terminate")
    public String terminateEmployee(
            @PathVariable Long id) {

        service.terminateEmployee(id);

        return "Employee terminated successfully";
    }
}