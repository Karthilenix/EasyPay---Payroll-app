package com.hexaware.easypay.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hexaware.easypay.dto.PaymentDTO;
import com.hexaware.easypay.entity.Payment;
import com.hexaware.easypay.entity.Payroll;
import com.hexaware.easypay.enums.PaymentStatus;
import com.hexaware.easypay.enums.PayrollStatus;
import com.hexaware.easypay.exception.InvalidPayrollStateException;
import com.hexaware.easypay.exception.ResourceNotFoundException;
import com.hexaware.easypay.repository.PaymentRepository;
import com.hexaware.easypay.repository.PayrollRepository;
import com.hexaware.easypay.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl
        implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PayrollRepository payrollRepository;

    @Override
    public Payment createPayment(
            PaymentDTO dto) {

        Payroll payroll =
                payrollRepository.findById(
                        dto.getPayrollId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payroll not found"));

        if(payroll.getStatus()
                != PayrollStatus.PROCESSED) {

            throw new InvalidPayrollStateException(
                    "Only processed payroll can be paid");
        }

        paymentRepository
                .findByPayrollPayrollId(
                        dto.getPayrollId())
                .ifPresent(payment -> {

                    throw new InvalidPayrollStateException(
                            "Payment already exists for this payroll");
                });

        Payment payment =
                new Payment();

        payment.setPayroll(payroll);

        payment.setAmount(
                payroll.getNetSalary());

        payment.setPaymentDate(
                LocalDate.now());

        payment.setPaymentMethod(
                dto.getPaymentMethod());

        payment.setStatus(
                PaymentStatus.PENDING);

        payment.setTransactionReference(
                UUID.randomUUID()
                        .toString());

        return paymentRepository.save(
                payment);
    }

    @Override
    public Payment getPaymentById(
            Long paymentId) {

        return paymentRepository.findById(
                paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found"));
    }

    @Override
    public List<Payment> getAllPayments() {

        return paymentRepository.findAll();
    }

    @Override
    public Payment markSuccess(
            Long paymentId) {

        Payment payment =
                getPaymentById(paymentId);

        payment.setStatus(
                PaymentStatus.SUCCESS);

        Payroll payroll =
                payment.getPayroll();

        payroll.setStatus(
                PayrollStatus.PAID);

        return paymentRepository.save(
                payment);
    }

    @Override
    public Payment markFailed(
            Long paymentId) {

        Payment payment =
                getPaymentById(paymentId);

        payment.setStatus(
                PaymentStatus.FAILED);

        return paymentRepository.save(
                payment);
    }
}