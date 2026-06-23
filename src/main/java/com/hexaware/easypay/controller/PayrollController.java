package com.hexaware.easypay.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.hexaware.easypay.dto.PayrollDTO;
import com.hexaware.easypay.entity.Payroll;
import com.hexaware.easypay.service.PayrollService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payrolls")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService service;

    @PostMapping
    public Payroll generatePayroll(
            @Valid @RequestBody PayrollDTO dto) {

        return service.generatePayroll(dto);
    }

    @GetMapping("/{id}")
    public Payroll getPayroll(
            @PathVariable Long id) {

        return service.getPayrollById(id);
    }

    @GetMapping
    public List<Payroll> getAllPayrolls() {

        return service.getAllPayrolls();
    }

    @GetMapping("/employee/{employeeId}")
    public List<Payroll> getEmployeePayrolls(
            @PathVariable Long employeeId) {

        return service.getPayrollsByEmployee(
                employeeId);
    }

    @PutMapping("/{id}/verify")
    public Payroll verifyPayroll(
            @PathVariable Long id) {

        return service.verifyPayroll(id);
    }

    @PutMapping("/{id}/process")
    public Payroll processPayroll(
            @PathVariable Long id) {

        return service.processPayroll(id);
    }
}