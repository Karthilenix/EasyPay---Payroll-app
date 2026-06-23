package com.hexaware.easypay.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hexaware.easypay.enums.EmployeeStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    private String designation;

    private Double basicSalary;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id",nullable = false)
    private Department department;
    
    @OneToOne(mappedBy = "employee",
            cascade = CascadeType.ALL)
	@JsonIgnore
	private User user;
    
    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<Payroll> payrolls;
    
    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<LeaveRequest> leaveRequests;
    
}