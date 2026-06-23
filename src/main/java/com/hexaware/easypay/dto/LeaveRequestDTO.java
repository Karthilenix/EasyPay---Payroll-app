package com.hexaware.easypay.dto;

import java.time.LocalDate;

import com.hexaware.easypay.enums.LeaveType;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LeaveRequestDTO {

    @NotNull(message = "Leave type is required")
    private LeaveType leaveType;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotBlank(message = "Reason is required")
    @Size(min = 5, max = 500,
          message = "Reason should contain 5 to 500 characters")
    private String reason;

    @NotNull(message = "Employee Id is required")
    private Long employeeId;
}