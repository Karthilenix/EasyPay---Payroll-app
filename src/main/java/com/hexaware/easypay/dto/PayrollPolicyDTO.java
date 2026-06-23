package com.hexaware.easypay.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PayrollPolicyDTO {

    @NotBlank(message = "Policy name is required")
    @Size(min = 3, max = 50)
    private String policyName;

    @NotNull(message = "Tax percentage is required")
    @Min(value = 0, message = "Tax cannot be negative")
    @Max(value = 100, message = "Tax cannot exceed 100")
    private Double taxPercentage;

    @NotNull(message = "PF percentage is required")
    @Min(value = 0, message = "PF cannot be negative")
    @Max(value = 100, message = "PF cannot exceed 100")
    private Double pfPercentage;

    @NotNull(message = "Bonus percentage is required")
    @Min(value = 0, message = "Bonus cannot be negative")
    @Max(value = 100, message = "Bonus cannot exceed 100")
    private Double bonusPercentage;

    @NotNull(message = "Overtime rate is required")
    @Positive(message = "Overtime rate must be positive")
    private Double overtimeRate;

    @NotNull(message = "Effective date is required")
    private LocalDate effectiveFrom;
}