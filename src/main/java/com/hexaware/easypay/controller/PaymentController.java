package com.hexaware.easypay.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.hexaware.easypay.dto.PaymentDTO;
import com.hexaware.easypay.entity.Payment;
import com.hexaware.easypay.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    public Payment createPayment(
            @Valid @RequestBody PaymentDTO dto) {

        return service.createPayment(dto);
    }

    @GetMapping("/{id}")
    public Payment getPayment(
            @PathVariable Long id) {

        return service.getPaymentById(id);
    }

    @GetMapping
    public List<Payment> getAllPayments() {

        return service.getAllPayments();
    }

    @PutMapping("/{id}/success")
    public Payment markSuccess(
            @PathVariable Long id) {

        return service.markSuccess(id);
    }

    @PutMapping("/{id}/failed")
    public Payment markFailed(
            @PathVariable Long id) {

        return service.markFailed(id);
    }
}