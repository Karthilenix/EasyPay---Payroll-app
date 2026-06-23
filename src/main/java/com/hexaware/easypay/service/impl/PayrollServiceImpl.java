package com.hexaware.easypay.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

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
import com.hexaware.easypay.service.PayrollService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl
        implements PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final PayrollPolicyRepository policyRepository;

    @Override
    public Payroll generatePayroll(
            PayrollDTO dto) {

        Employee employee =
                employeeRepository.findById(
                        dto.getEmployeeId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found"));
        
        if(payrollRepository
                .existsByEmployeeEmployeeIdAndPayPeriod(
                        dto.getEmployeeId(),
                        dto.getPayPeriod())) {

            throw new InvalidPayrollStateException(
                    "Payroll already generated for this employee and pay period");
        }
        
        if(employee.getStatus()
                != EmployeeStatus.ACTIVE) {

            throw new RuntimeException(
                    "Payroll can only be generated for active employees");
        }

        PayrollPolicy policy =
                policyRepository.findByActiveTrue()
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No active payroll policy found"));

        double basicSalary =
                employee.getBasicSalary();

        double bonusAmount =
                basicSalary *
                        policy.getBonusPercentage()
                        / 100;

        double overtimeAmount =
                dto.getOvertimeHours() *
                        policy.getOvertimeRate();

        double grossSalary =
                basicSalary +
                        bonusAmount +
                        overtimeAmount;

        double taxAmount =
                grossSalary *
                        policy.getTaxPercentage()
                        / 100;

        double pfAmount =
                grossSalary *
                        policy.getPfPercentage()
                        / 100;

        double netSalary =
                grossSalary -
                        taxAmount -
                        pfAmount;

        Payroll payroll =
                new Payroll();

        payroll.setPayPeriod(
                dto.getPayPeriod());

        payroll.setBasicSalary(
                basicSalary);

        payroll.setBonusAmount(
                bonusAmount);

        payroll.setOvertimeHours(
                dto.getOvertimeHours());

        payroll.setOvertimeAmount(
                overtimeAmount);

        payroll.setGrossSalary(
                grossSalary);

        payroll.setTaxAmount(
                taxAmount);

        payroll.setPfAmount(
                pfAmount);

        payroll.setNetSalary(
                netSalary);

        payroll.setGeneratedDate(
                LocalDate.now());

        payroll.setStatus(
                PayrollStatus.GENERATED);

        payroll.setEmployee(
                employee);

        payroll.setPayrollPolicy(
                policy);

        return payrollRepository.save(
                payroll);
    }

    @Override
    public Payroll getPayrollById(
            Long payrollId) {

        return payrollRepository.findById(
                payrollId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payroll not found"));
    }

    @Override
    public List<Payroll> getAllPayrolls() {

        return payrollRepository.findAll();
    }

    @Override
    public List<Payroll> getPayrollsByEmployee(
            Long employeeId) {

        return payrollRepository
                .findByEmployeeEmployeeId(
                        employeeId);
    }

    @Override
    public Payroll verifyPayroll(
            Long payrollId) {

        Payroll payroll =
                getPayrollById(payrollId);

        if(payroll.getStatus()
                != PayrollStatus.GENERATED) {

            throw new InvalidPayrollStateException(
                    "Only generated payroll can be verified");
        }

        payroll.setStatus(
                PayrollStatus.VERIFIED);

        return payrollRepository.save(
                payroll);
    }
    
    @Override
    public Payroll processPayroll(
            Long payrollId) {

        Payroll payroll =
                getPayrollById(payrollId);

        if(payroll.getStatus()
                != PayrollStatus.VERIFIED) {

            throw new InvalidPayrollStateException(
                    "Payroll must be verified before processing");
        }

        payroll.setStatus(
                PayrollStatus.PROCESSED);

        return payrollRepository.save(
                payroll);
    }
}