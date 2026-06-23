package com.hexaware.easypay.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeeDTO {

    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 30,
          message = "First name must contain 3 to 30 characters")
    @Pattern(
            regexp = "^[A-Z][a-zA-Z]*$",
            message = "First name must start with a capital letter"
    )
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 3, max = 30,
          message = "Last name must contain 3 to 30 characters")
    @Pattern(
            regexp = "^[A-Z][a-zA-Z]*$",
            message = "Last name must start with a capital letter"
    )
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Phone number must contain 10 digits and start with 6-9"
    )
    private String phoneNumber;

    @NotBlank(message = "Designation is required")
    @Size(min = 2, max = 50,
          message = "Designation must contain 2 to 50 characters")
    @Pattern(
            regexp = "^[A-Z][a-zA-Z ]*$",
            message = "Designation must start with a capital letter"
    )
    private String designation;

    @Positive(message = "Salary must be greater than zero")
    private Double basicSalary;

    @NotNull(message = "Department Id is required")
    private Long departmentId;
}