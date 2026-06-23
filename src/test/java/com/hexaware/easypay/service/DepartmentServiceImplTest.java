package com.hexaware.easypay.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.hexaware.easypay.entity.Department;
import com.hexaware.easypay.repository.DepartmentRepository;
import com.hexaware.easypay.service.impl.DepartmentServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository repository;

    @InjectMocks
    private DepartmentServiceImpl service;

    @Test
    void testGetDepartmentById() {

        Department department =
                new Department();

        department.setDepartmentId(1L);
        department.setDepartmentName("IT");

        when(repository.findById(1L))
                .thenReturn(
                        Optional.of(department));

        Department result =
                service.getDepartmentById(1L);

        assertNotNull(result);

        assertEquals(
                "IT",
                result.getDepartmentName());
    }
}
