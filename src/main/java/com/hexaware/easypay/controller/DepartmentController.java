package com.hexaware.easypay.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hexaware.easypay.dto.DepartmentDTO;
import com.hexaware.easypay.entity.Department;
import com.hexaware.easypay.service.DepartmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Validated
public class DepartmentController {

    private final DepartmentService service;

    @PostMapping
    public Department addDepartment(
            @Valid
            @RequestBody DepartmentDTO dto) {

        return service.addDepartment(dto);
    }

    @GetMapping("/{id}")
    public Department getDepartment(
            @PathVariable Long id) {

        return service.getDepartmentById(id);
    }

    @GetMapping
    public List<Department> getAllDepartments() {

        return service.getAllDepartments();
    }

    @PutMapping("/{id}")
    public Department updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentDTO dto) {

        return service.updateDepartment(id, dto);
    }

    @DeleteMapping("/{id}")
    public String deleteDepartment(
            @PathVariable Long id) {

        service.deleteDepartment(id);

        return "Department Deleted Successfully";
    }
}