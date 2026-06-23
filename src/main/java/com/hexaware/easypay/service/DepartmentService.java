package com.hexaware.easypay.service;

import java.util.List;

import com.hexaware.easypay.dto.DepartmentDTO;
import com.hexaware.easypay.entity.Department;

public interface DepartmentService {

    Department addDepartment(DepartmentDTO departmentDTO);

    Department getDepartmentById(Long departmentId);

    List<Department> getAllDepartments();

    Department updateDepartment(Long departmentId,
                                DepartmentDTO departmentDTO);

    void deleteDepartment(Long departmentId);

}