package com.hexaware.easypay.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hexaware.easypay.dto.PaymentDTO;
import com.hexaware.easypay.entity.Payment;
import com.hexaware.easypay.entity.Payroll;
import com.hexaware.easypay.enums.PaymentMethod;
import com.hexaware.easypay.enums.PaymentStatus;
import com.hexaware.easypay.enums.PayrollStatus;
import com.hexaware.easypay.exception.InvalidPayrollStateException;
import com.hexaware.easypay.exception.ResourceNotFoundException;
import com.hexaware.easypay.repository.PaymentRepository;
import com.hexaware.easypay.repository.PayrollRepository;
import com.hexaware.easypay.service.impl.PaymentServiceImpl;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PayrollRepository payrollRepository;

    @InjectMocks
    private PaymentServiceImpl service;

    @Test
    void testCreatePayment() {

        Payroll payroll = new Payroll();

        payroll.setPayrollId(1L);
        payroll.setStatus(PayrollStatus.PROCESSED);
        payroll.setNetSalary(50000.0);

        PaymentDTO dto = new PaymentDTO();

        dto.setPayrollId(1L);
        dto.setPaymentMethod(
                PaymentMethod.BANK_TRANSFER);

        when(payrollRepository.findById(1L))
                .thenReturn(Optional.of(payroll));

        when(paymentRepository.findByPayrollPayrollId(1L))
                .thenReturn(Optional.empty());

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result =
                service.createPayment(dto);

        assertNotNull(result);

        assertEquals(
                PaymentStatus.PENDING,
                result.getStatus());

        assertEquals(
                50000.0,
                result.getAmount());
    }

    @Test
    void testPayrollNotFound() {

        PaymentDTO dto = new PaymentDTO();

        dto.setPayrollId(100L);

        when(payrollRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.createPayment(dto));
    }

    @Test
    void testPayrollNotProcessed() {

        Payroll payroll = new Payroll();

        payroll.setPayrollId(1L);
        payroll.setStatus(PayrollStatus.GENERATED);

        PaymentDTO dto = new PaymentDTO();

        dto.setPayrollId(1L);

        when(payrollRepository.findById(1L))
                .thenReturn(Optional.of(payroll));

        assertThrows(
                InvalidPayrollStateException.class,
                () -> service.createPayment(dto));
    }

    @Test
    void testDuplicatePayment() {

        Payroll payroll = new Payroll();

        payroll.setPayrollId(1L);
        payroll.setStatus(PayrollStatus.PROCESSED);

        Payment payment = new Payment();

        payment.setPaymentId(1L);

        PaymentDTO dto = new PaymentDTO();

        dto.setPayrollId(1L);

        when(payrollRepository.findById(1L))
                .thenReturn(Optional.of(payroll));

        when(paymentRepository.findByPayrollPayrollId(1L))
                .thenReturn(Optional.of(payment));

        assertThrows(
                InvalidPayrollStateException.class,
                () -> service.createPayment(dto));
    }

    @Test
    void testMarkSuccess() {

        Payroll payroll = new Payroll();

        payroll.setPayrollId(1L);
        payroll.setStatus(PayrollStatus.PROCESSED);

        Payment payment = new Payment();

        payment.setPaymentId(1L);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPayroll(payroll);

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));

        when(paymentRepository.save(any(Payment.class)))
                .thenReturn(payment);

        Payroll updated =
                payment.getPayroll();

        Payment result =
                service.markSuccess(1L);

        assertEquals(
                PaymentStatus.SUCCESS,
                result.getStatus());

        assertEquals(
                PayrollStatus.PAID,
                updated.getStatus());
    }

    @Test
    void testMarkFailed() {

        Payment payment = new Payment();

        payment.setPaymentId(1L);
        payment.setStatus(PaymentStatus.PENDING);

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));

        when(paymentRepository.save(any(Payment.class)))
                .thenReturn(payment);

        Payment result =
                service.markFailed(1L);

        assertEquals(
                PaymentStatus.FAILED,
                result.getStatus());
    }

    @Test
    void testPaymentNotFound() {

        when(paymentRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.getPaymentById(100L));
    }
}