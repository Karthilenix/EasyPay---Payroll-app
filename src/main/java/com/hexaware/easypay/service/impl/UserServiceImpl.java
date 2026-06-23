package com.hexaware.easypay.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hexaware.easypay.dto.UserDTO;
import com.hexaware.easypay.entity.Employee;
import com.hexaware.easypay.entity.User;
import com.hexaware.easypay.enums.EmployeeStatus;
import com.hexaware.easypay.exception.DuplicateResourceException;
import com.hexaware.easypay.exception.ResourceNotFoundException;
import com.hexaware.easypay.repository.EmployeeRepository;
import com.hexaware.easypay.repository.UserRepository;
import com.hexaware.easypay.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public User addUser(UserDTO dto) {

        userRepository.findByUsername(dto.getUsername())
                .ifPresent(user -> {
                    throw new DuplicateResourceException(
                            "Username already exists");
                });

        Employee employee =
                employeeRepository.findById(
                        dto.getEmployeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found"));

        User user = new User();

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setEmployee(employee);

        log.info("User created successfully");

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));
    }

    @Override
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long userId,
                           UserDTO dto) {

        User user = getUserById(userId);

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());

        return userRepository.save(user);
    }

    @Override
    public void disableUser(Long userId) {

        User user = getUserById(userId);

        user.setActive(false);

        Employee employee = user.getEmployee();

        employee.setStatus(EmployeeStatus.INACTIVE);

        employeeRepository.save(employee);

        userRepository.save(user);
    }
}