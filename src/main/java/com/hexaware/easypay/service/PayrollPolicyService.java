package com.hexaware.easypay.service;

import java.util.List;

import com.hexaware.easypay.dto.PayrollPolicyDTO;
import com.hexaware.easypay.entity.PayrollPolicy;

public interface PayrollPolicyService {

    PayrollPolicy addPolicy(
            PayrollPolicyDTO dto);

    PayrollPolicy getPolicyById(
            Long policyId);

    List<PayrollPolicy> getAllPolicies();

    PayrollPolicy updatePolicy(
            Long policyId,
            PayrollPolicyDTO dto);

    PayrollPolicy activatePolicy(
            Long policyId);
}