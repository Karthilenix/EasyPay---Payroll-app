package com.hexaware.easypay.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payroll_policies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayrollPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long policyId;

    @Column(nullable = false, unique = true)
    private String policyName;

    @Column(nullable = false)
    private Double taxPercentage;

    @Column(nullable = false)
    private Double pfPercentage;

    @Column(nullable = false)
    private Double bonusPercentage;

    @Column(nullable = false)
    private Double overtimeRate;

    @Column(nullable = false)
    private LocalDate effectiveFrom;

    @Column(nullable = false)
    private boolean active;
    
    @OneToMany(mappedBy = "payrollPolicy")
    @JsonIgnore
    private List<Payroll> payrolls;
}