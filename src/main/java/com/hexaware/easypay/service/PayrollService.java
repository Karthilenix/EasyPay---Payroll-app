package com.hexaware.easypay.service;

import java.util.List;

import com.hexaware.easypay.dto.PayrollDTO;
import com.hexaware.easypay.entity.Payroll;

public interface PayrollService {

    Payroll generatePayroll(
            PayrollDTO dto);

    Payroll getPayrollById(
            Long payrollId);

    List<Payroll> getAllPayrolls();

    List<Payroll> getPayrollsByEmployee(
            Long employeeId);

    Payroll verifyPayroll(
            Long payrollId);

    Payroll processPayroll(
            Long payrollId);
}