package com.hexaware.easypay.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hexaware.easypay.enums.PayrollStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payrolls")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payrollId;

    @Column(nullable = false)
    private LocalDate payPeriod;

    @Column(nullable = false)
    private Double basicSalary;

    @Column(nullable = false)
    private Double bonusAmount;

    @Column(nullable = false)
    private Double overtimeHours;

    @Column(nullable = false)
    private Double overtimeAmount;

    @Column(nullable = false)
    private Double taxAmount;

    @Column(nullable = false)
    private Double pfAmount;

    @Column(nullable = false)
    private Double grossSalary;

    @Column(nullable = false)
    private Double netSalary;

    private LocalDate generatedDate;

    @Enumerated(EnumType.STRING)
    private PayrollStatus status;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "policy_id")
    private PayrollPolicy payrollPolicy;
    
    @OneToOne(mappedBy = "payroll")
    @JsonIgnore
    private Payment payment;
}