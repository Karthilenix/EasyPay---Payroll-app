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

import com.hexaware.easypay.dto.PayrollDTO;
import com.hexaware.easypay.entity.Employee;
import com.hexaware.easypay.entity.Payroll;
import com.hexaware.easypay.entity.PayrollPolicy;
import com.hexaware.easypay.enums.EmployeeStatus;
import com.hexaware.easypay.enums.PayrollStatus;
import com.hexaware.easypay.exception.InvalidPayrollStateException;
import com.hexaware.easypay.exception.ResourceNotFoundException;
import com.hexaware.easypay.repository.EmployeeRepository;
import com.hexaware.easypay.repository.PayrollPolicyRepository;
import com.hexaware.easypay.repository.PayrollRepository;
import com.hexaware.easypay.service.impl.PayrollServiceImpl;

@ExtendWith(MockitoExtension.class)
class PayrollServiceImplTest {

    @Mock
    private PayrollRepository payrollRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PayrollPolicyRepository policyRepository;

    @InjectMocks
    private PayrollServiceImpl service;

    @Test
    void testGeneratePayroll() {

        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee.setBasicSalary(50000.0);

        PayrollPolicy policy = new PayrollPolicy();
        policy.setPolicyId(1L);
        policy.setBonusPercentage(5.0);
        policy.setTaxPercentage(10.0);
        policy.setPfPercentage(12.0);
        policy.setOvertimeRate(500.0);

        PayrollDTO dto = new PayrollDTO();
        dto.setEmployeeId(1L);
        dto.setPayPeriod(LocalDate.of(2026, 7, 1));
        dto.setOvertimeHours(10.0);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(policyRepository.findByActiveTrue())
                .thenReturn(Optional.of(policy));

        when(payrollRepository.existsByEmployeeEmployeeIdAndPayPeriod(
                1L,
                LocalDate.of(2026, 7, 1)))
                .thenReturn(false);

        when(payrollRepository.save(any(Payroll.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payroll result = service.generatePayroll(dto);

        assertNotNull(result);

        assertEquals(
                PayrollStatus.GENERATED,
                result.getStatus());

        assertEquals(
                57500.0,
                result.getGrossSalary());

        assertEquals(
                44850.0,
                result.getNetSalary());
    }

    @Test
    void testEmployeeNotFound() {

        PayrollDTO dto = new PayrollDTO();
        dto.setEmployeeId(100L);

        when(employeeRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.generatePayroll(dto));
    }

    @Test
    void testInactiveEmployee() {

        Employee employee = new Employee();

        employee.setEmployeeId(1L);
        employee.setStatus(EmployeeStatus.INACTIVE);

        PayrollDTO dto = new PayrollDTO();
        dto.setEmployeeId(1L);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        assertThrows(
                RuntimeException.class,
                () -> service.generatePayroll(dto));
    }

    @Test
    void testNoActivePayrollPolicy() {

        Employee employee = new Employee();

        employee.setEmployeeId(1L);
        employee.setStatus(EmployeeStatus.ACTIVE);

        PayrollDTO dto = new PayrollDTO();
        dto.setEmployeeId(1L);
        dto.setPayPeriod(LocalDate.now());

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(policyRepository.findByActiveTrue())
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.generatePayroll(dto));
    }

    @Test
    void testDuplicatePayroll() {

        PayrollDTO dto = new PayrollDTO();

        dto.setEmployeeId(1L);
        dto.setPayPeriod(LocalDate.of(2026, 7, 1));

        Employee employee = new Employee();

        employee.setEmployeeId(1L);
        employee.setStatus(EmployeeStatus.ACTIVE);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(payrollRepository
                .existsByEmployeeEmployeeIdAndPayPeriod(
                        1L,
                        LocalDate.of(2026, 7, 1)))
                .thenReturn(true);

        assertThrows(
                InvalidPayrollStateException.class,
                () -> service.generatePayroll(dto));
    }

    @Test
    void testVerifyPayroll() {

        Payroll payroll = new Payroll();

        payroll.setPayrollId(1L);
        payroll.setStatus(PayrollStatus.GENERATED);

        when(payrollRepository.findById(1L))
                .thenReturn(Optional.of(payroll));

        when(payrollRepository.save(any()))
                .thenReturn(payroll);

        Payroll result =
                service.verifyPayroll(1L);

        assertEquals(
                PayrollStatus.VERIFIED,
                result.getStatus());
    }

    @Test
    void testVerifyAlreadyVerifiedPayroll() {

        Payroll payroll = new Payroll();

        payroll.setPayrollId(1L);
        payroll.setStatus(PayrollStatus.VERIFIED);

        when(payrollRepository.findById(1L))
                .thenReturn(Optional.of(payroll));

        assertThrows(
                InvalidPayrollStateException.class,
                () -> service.verifyPayroll(1L));
    }

    @Test
    void testProcessPayroll() {

        Payroll payroll = new Payroll();

        payroll.setPayrollId(1L);
        payroll.setStatus(PayrollStatus.VERIFIED);

        when(payrollRepository.findById(1L))
                .thenReturn(Optional.of(payroll));

        when(payrollRepository.save(any()))
                .thenReturn(payroll);

        Payroll result =
                service.processPayroll(1L);

        assertEquals(
                PayrollStatus.PROCESSED,
                result.getStatus());
    }

    @Test
    void testProcessWithoutVerification() {

        Payroll payroll = new Payroll();

        payroll.setPayrollId(1L);
        payroll.setStatus(PayrollStatus.GENERATED);

        when(payrollRepository.findById(1L))
                .thenReturn(Optional.of(payroll));

        assertThrows(
                InvalidPayrollStateException.class,
                () -> service.processPayroll(1L));
    }
}