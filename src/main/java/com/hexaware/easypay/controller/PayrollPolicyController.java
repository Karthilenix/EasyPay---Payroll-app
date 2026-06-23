package com.hexaware.easypay.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.hexaware.easypay.dto.PayrollPolicyDTO;
import com.hexaware.easypay.entity.PayrollPolicy;
import com.hexaware.easypay.service.PayrollPolicyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payroll-policies")
@RequiredArgsConstructor
public class PayrollPolicyController {

    private final PayrollPolicyService service;

    @PostMapping
    public PayrollPolicy addPolicy(
            @Valid @RequestBody PayrollPolicyDTO dto) {

        return service.addPolicy(dto);
    }

    @GetMapping("/{id}")
    public PayrollPolicy getPolicy(
            @PathVariable Long id) {

        return service.getPolicyById(id);
    }

    @GetMapping
    public List<PayrollPolicy> getAllPolicies() {

        return service.getAllPolicies();
    }

    @PutMapping("/{id}")
    public PayrollPolicy updatePolicy(
            @PathVariable Long id,
            @Valid @RequestBody PayrollPolicyDTO dto) {

        return service.updatePolicy(id, dto);
    }

    @PutMapping("/{id}/activate")
    public PayrollPolicy activatePolicy(
            @PathVariable Long id) {

        return service.activatePolicy(id);
    }
}