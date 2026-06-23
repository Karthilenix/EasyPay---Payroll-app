package com.hexaware.easypay.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.hexaware.easypay.entity.Employee;
import com.hexaware.easypay.enums.EmployeeStatus;
import com.hexaware.easypay.exception.ResourceNotFoundException;
import com.hexaware.easypay.repository.EmployeeRepository;
import com.hexaware.easypay.service.impl.EmployeeServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeServiceImpl service;

    @Test
    void testGetEmployeeById() {

        Employee employee = new Employee();

        employee.setEmployeeId(1L);
        employee.setFirstName("John");

        when(repository.findById(1L))
                .thenReturn(Optional.of(employee));

        Employee result =
                service.getEmployeeById(1L);

        assertNotNull(result);

        assertEquals(
                "John",
                result.getFirstName());
    }

    @Test
    void testEmployeeNotFound() {

        when(repository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.getEmployeeById(100L));
    }

    @Test
    void testTerminateEmployee() {

        Employee employee = new Employee();

        employee.setEmployeeId(1L);
        employee.setStatus(EmployeeStatus.ACTIVE);

        when(repository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(repository.save(any(Employee.class)))
                .thenReturn(employee);

        service.terminateEmployee(1L);

        assertEquals(
                EmployeeStatus.TERMINATED,
                employee.getStatus());
    }
}