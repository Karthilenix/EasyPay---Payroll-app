package com.hexaware.easypay.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.hexaware.easypay.entity.Employee;
import com.hexaware.easypay.entity.User;
import com.hexaware.easypay.enums.EmployeeStatus;
import com.hexaware.easypay.exception.ResourceNotFoundException;
import com.hexaware.easypay.repository.EmployeeRepository;
import com.hexaware.easypay.repository.UserRepository;
import com.hexaware.easypay.service.impl.UserServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void testGetUserById() {

        User user = new User();

        user.setUserId(1L);
        user.setUsername("john123");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        User result =
                service.getUserById(1L);

        assertNotNull(result);

        assertEquals(
                "john123",
                result.getUsername());
    }

    @Test
    void testUserNotFound() {

        when(userRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.getUserById(100L));
    }

    @Test
    void testDisableUser() {

        Employee employee =
                new Employee();

        employee.setEmployeeId(1L);

        employee.setStatus(
                EmployeeStatus.ACTIVE);

        User user =
                new User();

        user.setUserId(1L);

        user.setActive(true);

        user.setEmployee(employee);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(employee);

        service.disableUser(1L);

        assertFalse(user.isActive());

        assertEquals(
                EmployeeStatus.INACTIVE,
                employee.getStatus());
    }
}