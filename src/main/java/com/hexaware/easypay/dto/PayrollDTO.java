package com.hexaware.easypay.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class PayrollDTO {

    @NotNull(message = "Employee Id is required")
    private Long employeeId;

    @NotNull(message = "Pay period is required")
    private LocalDate payPeriod;

    @PositiveOrZero(message = "Overtime hours cannot be negative")
    private Double overtimeHours;
}