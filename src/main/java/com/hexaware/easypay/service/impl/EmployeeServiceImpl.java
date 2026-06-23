package com.hexaware.easypay.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hexaware.easypay.dto.EmployeeDTO;
import com.hexaware.easypay.entity.Department;
import com.hexaware.easypay.entity.Employee;
import com.hexaware.easypay.enums.EmployeeStatus;
import com.hexaware.easypay.exception.ResourceNotFoundException;
import com.hexaware.easypay.repository.DepartmentRepository;
import com.hexaware.easypay.repository.EmployeeRepository;
import com.hexaware.easypay.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl
        implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public Employee addEmployee(EmployeeDTO dto) {

        Department department =
                departmentRepository.findById(
                        dto.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department Not Found"));

        Employee employee = new Employee();

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setDesignation(dto.getDesignation());
        employee.setBasicSalary(dto.getBasicSalary());

        employee.setStatus(
                EmployeeStatus.ACTIVE);

        employee.setDepartment(department);

        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(Long employeeId) {

        return employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee Not Found"));
    }

    @Override
    public List<Employee> getAllEmployees() {

        return employeeRepository.findAll();
    }

    @Override
    public Employee updateEmployee(
            Long employeeId,
            EmployeeDTO dto) {

        Employee employee =
                getEmployeeById(employeeId);

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setDesignation(dto.getDesignation());
        employee.setBasicSalary(dto.getBasicSalary());

        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {

        employeeRepository.delete(
                getEmployeeById(employeeId));
    }
    @Override
    public void terminateEmployee(
            Long employeeId) {

        Employee employee =
                getEmployeeById(employeeId);

        employee.setStatus(
                EmployeeStatus.TERMINATED);

        employeeRepository.save(employee);
    }
}