package com.hexaware.easypay.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.hexaware.easypay.dto.LeaveRequestDTO;
import com.hexaware.easypay.entity.LeaveRequest;
import com.hexaware.easypay.service.LeaveRequestService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/leaves")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService service;

    @PostMapping
    public LeaveRequest applyLeave(
            @Valid @RequestBody LeaveRequestDTO dto) {

        return service.applyLeave(dto);
    }

    @GetMapping("/{id}")
    public LeaveRequest getLeave(
            @PathVariable Long id) {

        return service.getLeaveById(id);
    }

    @GetMapping
    public List<LeaveRequest> getAllLeaves() {

        return service.getAllLeaves();
    }

    @GetMapping("/employee/{employeeId}")
    public List<LeaveRequest> getEmployeeLeaves(
            @PathVariable Long employeeId) {

        return service.getLeavesByEmployee(
                employeeId);
    }

    @PutMapping("/{id}/approve")
    public LeaveRequest approveLeave(
            @PathVariable Long id,
            @RequestParam String comments) {

        return service.approveLeave(
                id,
                comments);
    }

    @PutMapping("/{id}/reject")
    public LeaveRequest rejectLeave(
            @PathVariable Long id,
            @RequestParam String comments) {

        return service.rejectLeave(
                id,
                comments);
    }
}