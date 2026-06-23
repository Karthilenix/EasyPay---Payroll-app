package com.hexaware.easypay.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hexaware.easypay.dto.PayrollPolicyDTO;
import com.hexaware.easypay.entity.PayrollPolicy;
import com.hexaware.easypay.exception.DuplicateResourceException;
import com.hexaware.easypay.exception.ResourceNotFoundException;
import com.hexaware.easypay.repository.PayrollPolicyRepository;
import com.hexaware.easypay.service.PayrollPolicyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayrollPolicyServiceImpl
        implements PayrollPolicyService {

    private final PayrollPolicyRepository repository;

    @Override
    public PayrollPolicy addPolicy(
            PayrollPolicyDTO dto) {

        repository.findByPolicyName(
                dto.getPolicyName())
                .ifPresent(policy -> {
                    throw new DuplicateResourceException(
                            "Policy already exists");
                });

        PayrollPolicy policy =
                new PayrollPolicy();

        policy.setPolicyName(
                dto.getPolicyName());

        policy.setTaxPercentage(
                dto.getTaxPercentage());

        policy.setPfPercentage(
                dto.getPfPercentage());

        policy.setBonusPercentage(
                dto.getBonusPercentage());

        policy.setOvertimeRate(
                dto.getOvertimeRate());

        policy.setEffectiveFrom(
                dto.getEffectiveFrom());

        policy.setActive(false);

        log.info("Payroll Policy Created");

        return repository.save(policy);
    }

    @Override
    public PayrollPolicy getPolicyById(
            Long policyId) {

        return repository.findById(policyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Policy not found"));
    }

    @Override
    public List<PayrollPolicy> getAllPolicies() {

        return repository.findAll();
    }

    @Override
    public PayrollPolicy updatePolicy(
            Long policyId,
            PayrollPolicyDTO dto) {

        PayrollPolicy policy =
                getPolicyById(policyId);

        policy.setPolicyName(
                dto.getPolicyName());

        policy.setTaxPercentage(
                dto.getTaxPercentage());

        policy.setPfPercentage(
                dto.getPfPercentage());

        policy.setBonusPercentage(
                dto.getBonusPercentage());

        policy.setOvertimeRate(
                dto.getOvertimeRate());

        policy.setEffectiveFrom(
                dto.getEffectiveFrom());

        return repository.save(policy);
    }

    @Override
    public PayrollPolicy activatePolicy(
            Long policyId) {

        repository.findByActiveTrue()
                .ifPresent(policy -> {

                    policy.setActive(false);

                    repository.save(policy);
                });

        PayrollPolicy policy =
                getPolicyById(policyId);

        policy.setActive(true);

        return repository.save(policy);
    }
}