package com.hexaware.easypay.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hexaware.easypay.dto.LeaveRequestDTO;
import com.hexaware.easypay.entity.Employee;
import com.hexaware.easypay.entity.LeaveRequest;
import com.hexaware.easypay.enums.EmployeeStatus;
import com.hexaware.easypay.enums.LeaveStatus;
import com.hexaware.easypay.enums.LeaveType;
import com.hexaware.easypay.exception.InvalidLeaveRequestException;
import com.hexaware.easypay.exception.ResourceNotFoundException;
import com.hexaware.easypay.repository.EmployeeRepository;
import com.hexaware.easypay.repository.LeaveRequestRepository;
import com.hexaware.easypay.service.impl.LeaveRequestServiceImpl;

@ExtendWith(MockitoExtension.class)
class LeaveRequestServiceImplTest {

    @Mock
    private LeaveRequestRepository leaveRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private LeaveRequestServiceImpl service;

    @Test
    void testApplyLeave() {

        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setStatus(EmployeeStatus.ACTIVE);

        LeaveRequestDTO dto = new LeaveRequestDTO();

        dto.setEmployeeId(1L);
        dto.setLeaveType(LeaveType.SICK);
        dto.setReason("Fever");
        dto.setStartDate(LocalDate.now().plusDays(1));
        dto.setEndDate(LocalDate.now().plusDays(2));

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(leaveRepository.save(any(LeaveRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LeaveRequest result = service.applyLeave(dto);

        assertNotNull(result);
        assertEquals(LeaveStatus.PENDING, result.getStatus());
    }

    @Test
    void testEmployeeNotFound() {

        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setEmployeeId(100L);

        when(employeeRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.applyLeave(dto));
    }

    @Test
    void testInactiveEmployeeCannotApplyLeave() {

        Employee employee = new Employee();

        employee.setEmployeeId(1L);
        employee.setStatus(EmployeeStatus.INACTIVE);

        LeaveRequestDTO dto = new LeaveRequestDTO();

        dto.setEmployeeId(1L);
        dto.setLeaveType(LeaveType.SICK);
        dto.setReason("Medical");
        dto.setStartDate(LocalDate.now().plusDays(1));
        dto.setEndDate(LocalDate.now().plusDays(2));

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        assertThrows(
                InvalidLeaveRequestException.class,
                () -> service.applyLeave(dto));
    }

    @Test
    void testApproveLeave() {

        LeaveRequest leave = new LeaveRequest();

        leave.setLeaveId(1L);
        leave.setStatus(LeaveStatus.PENDING);

        when(leaveRepository.findById(1L))
                .thenReturn(Optional.of(leave));

        when(leaveRepository.save(any(LeaveRequest.class)))
                .thenReturn(leave);

        LeaveRequest result =
                service.approveLeave(1L, "Approved");

        assertEquals(
                LeaveStatus.APPROVED,
                result.getStatus());
    }

    @Test
    void testRejectLeave() {

        LeaveRequest leave = new LeaveRequest();

        leave.setLeaveId(1L);
        leave.setStatus(LeaveStatus.PENDING);

        when(leaveRepository.findById(1L))
                .thenReturn(Optional.of(leave));

        when(leaveRepository.save(any(LeaveRequest.class)))
                .thenReturn(leave);

        LeaveRequest result =
                service.rejectLeave(1L, "Rejected");

        assertEquals(
                LeaveStatus.REJECTED,
                result.getStatus());
    }

    @Test
    void testLeaveNotFound() {

        when(leaveRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.getLeaveById(100L));
    }

    @Test
    void testAlreadyProcessedLeave() {

        LeaveRequest leave = new LeaveRequest();

        leave.setLeaveId(1L);
        leave.setStatus(LeaveStatus.APPROVED);

        when(leaveRepository.findById(1L))
                .thenReturn(Optional.of(leave));

        assertThrows(
                InvalidLeaveRequestException.class,
                () -> service.approveLeave(1L, "Approved"));
    }
}