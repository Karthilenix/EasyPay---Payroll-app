package com.hexaware.easypay.service;

import java.util.List;

import com.hexaware.easypay.dto.EmployeeDTO;
import com.hexaware.easypay.entity.Employee;

public interface EmployeeService {

    Employee addEmployee(EmployeeDTO dto);

    Employee getEmployeeById(Long employeeId);

    List<Employee> getAllEmployees();

    Employee updateEmployee(Long employeeId,
                            EmployeeDTO dto);
    
    void deleteEmployee(Long employeeId);
    
    void terminateEmployee(Long employeeId);
}