package com.hexaware.easypay.service;

import java.util.List;

import com.hexaware.easypay.dto.LeaveRequestDTO;
import com.hexaware.easypay.entity.LeaveRequest;

public interface LeaveRequestService {

    LeaveRequest applyLeave(
            LeaveRequestDTO dto);

    LeaveRequest getLeaveById(
            Long leaveId);

    List<LeaveRequest> getAllLeaves();

    List<LeaveRequest> getLeavesByEmployee(
            Long employeeId);

    LeaveRequest approveLeave(
            Long leaveId,
            String managerComments);

    LeaveRequest rejectLeave(
            Long leaveId,
            String managerComments);
}