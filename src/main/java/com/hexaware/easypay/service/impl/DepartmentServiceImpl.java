package com.hexaware.easypay.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hexaware.easypay.dto.DepartmentDTO;
import com.hexaware.easypay.entity.Department;
import com.hexaware.easypay.exception.DepartmentDeletionException;
import com.hexaware.easypay.exception.DuplicateResourceException;
import com.hexaware.easypay.exception.ResourceNotFoundException;
import com.hexaware.easypay.repository.DepartmentRepository;
import com.hexaware.easypay.repository.EmployeeRepository;
import com.hexaware.easypay.service.DepartmentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Department addDepartment(
            DepartmentDTO dto) {

        repository.findByDepartmentName(
                dto.getDepartmentName())
                .ifPresent(dept -> {
                    throw new DuplicateResourceException(
                            "Department already exists");
                });

        Department department = new Department();

        department.setDepartmentName(
                dto.getDepartmentName());

        department.setDepartmentHead(
                dto.getDepartmentHead());

        log.info("Department Created");

        return repository.save(department);
    }

    @Override
    public Department getDepartmentById(
            Long departmentId) {

        return repository.findById(departmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found"));
    }

    @Override
    public List<Department> getAllDepartments() {

        return repository.findAll();
    }

    @Override
    public Department updateDepartment(
            Long departmentId,
            DepartmentDTO dto) {

        Department department =
                getDepartmentById(departmentId);

        department.setDepartmentName(
                dto.getDepartmentName());

        department.setDepartmentHead(
                dto.getDepartmentHead());

        return repository.save(department);
    }

    @Override
    public void deleteDepartment(Long departmentId) {

        Department department =
                getDepartmentById(departmentId);

        if(employeeRepository
                .existsByDepartmentDepartmentId(
                        departmentId)) {

            throw new DepartmentDeletionException(
                    "Cannot delete department because employees are assigned to it");
        }

        repository.delete(department);
    }
}