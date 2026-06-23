package com.hexaware.easypay.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hexaware.easypay.dto.LeaveRequestDTO;
import com.hexaware.easypay.entity.Employee;
import com.hexaware.easypay.entity.LeaveRequest;
import com.hexaware.easypay.enums.EmployeeStatus;
import com.hexaware.easypay.enums.LeaveStatus;
import com.hexaware.easypay.exception.InvalidLeaveRequestException;
import com.hexaware.easypay.exception.ResourceNotFoundException;
import com.hexaware.easypay.repository.EmployeeRepository;
import com.hexaware.easypay.repository.LeaveRequestRepository;
import com.hexaware.easypay.service.LeaveRequestService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveRequestServiceImpl
        implements LeaveRequestService {

    private final LeaveRequestRepository leaveRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public LeaveRequest applyLeave(
            LeaveRequestDTO dto) {

        Employee employee =
                employeeRepository.findById(
                        dto.getEmployeeId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found"));
        
        if(employee.getStatus()
                != EmployeeStatus.ACTIVE) {

            throw new InvalidLeaveRequestException(
                    "Only active employees can apply for leave");
        }
        
        if(dto.getEndDate()
                .isBefore(dto.getStartDate())) {

            throw new InvalidLeaveRequestException(
                    "End date cannot be before start date");
        }
        
        if(dto.getStartDate()
                .isBefore(LocalDate.now())) {

            throw new InvalidLeaveRequestException(
                    "Leave cannot be applied for past dates");
        }

        LeaveRequest leave = new LeaveRequest();

        leave.setLeaveType(dto.getLeaveType());
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());

        leave.setStatus(
                LeaveStatus.PENDING);

        leave.setAppliedDate(
                LocalDate.now());

        leave.setEmployee(employee);

        log.info("Leave request submitted");

        return leaveRepository.save(leave);
    }

    @Override
    public LeaveRequest getLeaveById(
            Long leaveId) {

        return leaveRepository.findById(leaveId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Leave request not found"));
    }

    @Override
    public List<LeaveRequest> getAllLeaves() {

        return leaveRepository.findAll();
    }

    @Override
    public List<LeaveRequest> getLeavesByEmployee(
            Long employeeId) {

        return leaveRepository
                .findByEmployeeEmployeeId(
                        employeeId);
    }

    @Override
    public LeaveRequest approveLeave(
            Long leaveId,
            String comments) {
    	
        LeaveRequest leave =
                getLeaveById(leaveId);
        
        
        if(leave.getStatus()
                != LeaveStatus.PENDING) {

            throw new InvalidLeaveRequestException(
                    "Leave request already processed");
        }
        leave.setStatus(
                LeaveStatus.APPROVED);

        leave.setManagerComments(
                comments);

        leave.setApprovedBy(
                "Manager");

        leave.setDecisionDate(
                LocalDate.now());

        return leaveRepository.save(leave);
    }

    @Override
    public LeaveRequest rejectLeave(
            Long leaveId,
            String comments) {

        LeaveRequest leave =
                getLeaveById(leaveId);
        
        if(leave.getStatus()
                != LeaveStatus.PENDING) {

            throw new InvalidLeaveRequestException(
                    "Leave request already processed");
        }
        
        leave.setStatus(
                LeaveStatus.REJECTED);

        leave.setManagerComments(
                comments);

        leave.setApprovedBy(
                "Manager");

        leave.setDecisionDate(
                LocalDate.now());

        return leaveRepository.save(leave);
    }
}