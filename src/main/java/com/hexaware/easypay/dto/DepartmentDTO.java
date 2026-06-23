package com.hexaware.easypay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DepartmentDTO {

    @NotBlank(message = "Department name is required")
    @Size(min = 2, max = 50,
          message = "Department name must contain 2 to 50 characters")
    @Pattern(
            regexp = "^[A-Z][a-zA-Z ]*$",
            message = "Department name must start with a capital letter"
    )
    private String departmentName;

    @NotBlank(message = "Department head is required")
    @Size(min = 3, max = 50,
          message = "Department head name must contain 3 to 50 characters")
    @Pattern(
            regexp = "^[A-Z][a-zA-Z ]*$",
            message = "Department head name must start with a capital letter"
    )
    private String departmentHead;
}