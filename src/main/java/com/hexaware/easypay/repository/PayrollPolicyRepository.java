package com.hexaware.easypay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexaware.easypay.entity.PayrollPolicy;

public interface PayrollPolicyRepository
        extends JpaRepository<PayrollPolicy, Long> {

    Optional<PayrollPolicy> findByPolicyName(
            String policyName);

    Optional<PayrollPolicy> findByActiveTrue();
}