package com.hexaware.easypay.service;

import java.util.List;

import com.hexaware.easypay.dto.PaymentDTO;
import com.hexaware.easypay.entity.Payment;

public interface PaymentService {

    Payment createPayment(
            PaymentDTO dto);

    Payment getPaymentById(
            Long paymentId);

    List<Payment> getAllPayments();

    Payment markSuccess(
            Long paymentId);

    Payment markFailed(
            Long paymentId);
}