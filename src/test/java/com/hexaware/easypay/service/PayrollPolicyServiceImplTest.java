package com.hexaware.easypay.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hexaware.easypay.dto.PayrollPolicyDTO;
import com.hexaware.easypay.entity.PayrollPolicy;
import com.hexaware.easypay.exception.DuplicateResourceException;
import com.hexaware.easypay.exception.ResourceNotFoundException;
import com.hexaware.easypay.repository.PayrollPolicyRepository;
import com.hexaware.easypay.service.impl.PayrollPolicyServiceImpl;

@ExtendWith(MockitoExtension.class)
class PayrollPolicyServiceImplTest {

    @Mock
    private PayrollPolicyRepository repository;

    @InjectMocks
    private PayrollPolicyServiceImpl service;

    @Test
    void testAddPolicy() {

        PayrollPolicyDTO dto = new PayrollPolicyDTO();

        dto.setPolicyName("Default Policy");
        dto.setTaxPercentage(10.0);
        dto.setPfPercentage(12.0);
        dto.setBonusPercentage(5.0);
        dto.setOvertimeRate(500.0);
        dto.setEffectiveFrom(LocalDate.now());

        when(repository.findByPolicyName("Default Policy"))
                .thenReturn(Optional.empty());

        when(repository.save(any(PayrollPolicy.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PayrollPolicy result = service.addPolicy(dto);

        assertNotNull(result);
        assertEquals("Default Policy", result.getPolicyName());
    }

    @Test
    void testDuplicatePolicy() {

        PayrollPolicy policy = new PayrollPolicy();

        policy.setPolicyId(1L);
        policy.setPolicyName("Default Policy");

        when(repository.findByPolicyName("Default Policy"))
                .thenReturn(Optional.of(policy));

        PayrollPolicyDTO dto = new PayrollPolicyDTO();

        dto.setPolicyName("Default Policy");

        assertThrows(
                DuplicateResourceException.class,
                () -> service.addPolicy(dto));
    }

    @Test
    void testGetPolicyById() {

        PayrollPolicy policy = new PayrollPolicy();

        policy.setPolicyId(1L);
        policy.setPolicyName("Default Policy");

        when(repository.findById(1L))
                .thenReturn(Optional.of(policy));

        PayrollPolicy result =
                service.getPolicyById(1L);

        assertEquals(
                "Default Policy",
                result.getPolicyName());
    }

    @Test
    void testPolicyNotFound() {

        when(repository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.getPolicyById(100L));
    }

    @Test
    void testActivatePolicy() {

        PayrollPolicy policy = new PayrollPolicy();

        policy.setPolicyId(1L);
        policy.setActive(false);

        when(repository.findById(1L))
                .thenReturn(Optional.of(policy));

        when(repository.findByActiveTrue())
                .thenReturn(Optional.empty());

        when(repository.save(any(PayrollPolicy.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PayrollPolicy result =
                service.activatePolicy(1L);

        assertTrue(result.isActive());
    }

    @Test
    void testDeactivatePreviousPolicy() {

        PayrollPolicy activePolicy =
                new PayrollPolicy();

        activePolicy.setPolicyId(1L);
        activePolicy.setActive(true);

        PayrollPolicy newPolicy =
                new PayrollPolicy();

        newPolicy.setPolicyId(2L);
        newPolicy.setActive(false);

        when(repository.findByActiveTrue())
                .thenReturn(Optional.of(activePolicy));

        when(repository.findById(2L))
                .thenReturn(Optional.of(newPolicy));

        when(repository.save(any(PayrollPolicy.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PayrollPolicy result =
                service.activatePolicy(2L);

        assertFalse(activePolicy.isActive());

        assertTrue(result.isActive());
    }
}