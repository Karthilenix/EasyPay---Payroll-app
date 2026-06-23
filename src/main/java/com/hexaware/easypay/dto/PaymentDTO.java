package com.hexaware.easypay.dto;

import com.hexaware.easypay.enums.PaymentMethod;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentDTO {

    @NotNull(message = "Payroll Id is required")
    private Long payrollId;

    @NotNull(message = "Payment Method is required")
    private PaymentMethod paymentMethod;
}