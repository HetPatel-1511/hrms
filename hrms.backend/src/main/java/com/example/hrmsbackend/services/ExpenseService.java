package com.example.hrmsbackend.services;

import com.example.hrmsbackend.dtos.request.CustomUserDetails;
import com.example.hrmsbackend.dtos.request.ExpenseCreateRequestDTO;
import com.example.hrmsbackend.dtos.request.ExpenseStatusRequestDTO;
import com.example.hrmsbackend.dtos.response.ExpenseListResponseDTO;
import com.example.hrmsbackend.dtos.response.ExpenseResponseDTO;
import com.example.hrmsbackend.entities.*;
import com.example.hrmsbackend.exceptions.ResourceNotFoundException;
import com.example.hrmsbackend.mappers.EntityMapper;
import com.example.hrmsbackend.repos.*;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    private ExpenseRepo expenseRepo;
    private TravelPlanEmployeeRepo travelPlanEmployeeRepo;
    private MediaService mediaService;
    private EntityMapper entityMapper;
    private EmailService emailService;
    private EmployeeRepo employeeRepo;
    private TravelPlanRepo travelPlanRepo;
    private ConfigurationRepo configurationRepo;

    @Autowired
    public ExpenseService(ExpenseRepo expenseRepo, TravelPlanEmployeeRepo travelPlanEmployeeRepo, MediaService mediaService, EntityMapper entityMapper, EmailService emailService, EmployeeRepo employeeRepo, TravelPlanRepo travelPlanRepo, ConfigurationRepo configurationRepo) {
        this.expenseRepo = expenseRepo;
        this.travelPlanEmployeeRepo = travelPlanEmployeeRepo;
        this.mediaService = mediaService;
        this.entityMapper = entityMapper;
        this.emailService = emailService;
        this.employeeRepo = employeeRepo;
        this.travelPlanRepo = travelPlanRepo;
        this.configurationRepo = configurationRepo;
    }

    @Transactional
    public ExpenseResponseDTO create(ExpenseCreateRequestDTO dto, CustomUserDetails userDetails) {
        Result result = getTravelPlanEmployeeDetails(dto.getTravelPlanId(), dto.getEmployeeId());
        TravelPlanEmployee travelPlanEmployee = getTravelPlanEmployee(result.travelPlanEmployeeId);

        validateSubmissionWindow(travelPlanEmployee);

        Expense expense = getExpense(dto, travelPlanEmployee);
        saveMedia(dto, userDetails, expense);

        Expense savedExpense = expenseRepo.save(expense);

        return entityMapper.toExpenseResponseDTO(savedExpense);
    }

    private void saveMedia(ExpenseCreateRequestDTO dto, CustomUserDetails userDetails, Expense expense) {
        for(MultipartFile uploadedExpenseMedia: dto.getExpenseMedias()) {
            Media media = uploadProofFile(uploadedExpenseMedia, userDetails);
            ExpenseMedia newExpenseMedia = new ExpenseMedia();
            newExpenseMedia.setExpenseMedias(media);
            expense.addExpenseMedia(newExpenseMedia);
        }
    }

    private static @NonNull Expense getExpense(ExpenseCreateRequestDTO dto, TravelPlanEmployee travelPlanEmployee) {
        Expense expense = new Expense();
        expense.setTravelPlanEmployee(travelPlanEmployee);
        expense.setAmount(dto.getAmount());
        expense.setDescription(dto.getDescription());
        expense.setStatus("Draft");
        expense.setCreatedAt(LocalDateTime.now());
        return expense;
    }

    @Transactional
    public ExpenseResponseDTO changeExpenseStatus(ExpenseStatusRequestDTO dto, CustomUserDetails userDetails) {
        Expense expense = getExpense(dto.getExpenseId());
        validateStatusTransition(expense.getStatus(), dto.getStatus());
        validateUserRole(dto.getStatus(), userDetails);

        if ("Submitted".equals(dto.getStatus())) {
            validateSubmissionWindow(expense.getTravelPlanEmployee());
            sendSubmissionNotification(expense);
        }

        updateExpenseStatus(expense, dto, userDetails);
        expenseRepo.save(expense);

        return entityMapper.toExpenseResponseDTO(expense);
    }

    public ExpenseListResponseDTO getExpensesByTravelPlanEmployee(Long travelPlanId,Long employeeId, CustomUserDetails userDetails) {
        Result result = getTravelPlanEmployeeDetails(travelPlanId, employeeId);
        validateAccessPermission(result.travelPlanEmployee(), userDetails);

        List<Expense> expenses = expenseRepo.findByTravelPlanEmployeeId(result.travelPlanEmployeeId());
        List<ExpenseResponseDTO> expenseDTOs = entityMapper.toExpenseResponseDTOList(expenses);

        AmountResult amountResult = getAmountBreakdown(expenseDTOs);

        ExpenseListResponseDTO response = getExpenseListResponseDTO(expenseDTOs, amountResult, result);

        return response;
    }

    private @NonNull ExpenseListResponseDTO getExpenseListResponseDTO(List<ExpenseResponseDTO> expenseDTOs, AmountResult amountResult, Result result) {
        ExpenseListResponseDTO response = new ExpenseListResponseDTO();
        response.setExpenses(expenseDTOs);
        response.setTotalClaimedAmount(amountResult.totalClaimedAmount());
        response.setTotalUnclaimableAmount(amountResult.totalUnclaimableAmount());
        response.setTotalAmount(amountResult.totalAmount());
        response.setTotalCount((long) expenseDTOs.size());
        response.setTravelPlan(entityMapper.toTravelPlanSummaryResponseDTO(result.travelPlan()));
        response.setEmployee(entityMapper.toEmployeeSummaryDTO(result.employee()));
        return response;
    }

    private @NonNull Result getTravelPlanEmployeeDetails(Long travelPlanId, Long employeeId) {
        TravelPlan travelPlan = travelPlanRepo.findById(travelPlanId).orElseThrow(() -> new ResourceNotFoundException("Travel plan not found"));
        Employee employee = employeeRepo.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        TravelPlanEmployee travelPlanEmployee = travelPlanEmployeeRepo.findByTravelPlanAndEmployee(travelPlan, employee).orElseThrow(() -> new ResourceNotFoundException("Employee is not a part of the travel plan"));
        Long travelPlanEmployeeId = travelPlanEmployee.getId();
        Result result = new Result(travelPlan, employee, travelPlanEmployee, travelPlanEmployeeId);
        return result;
    }

    private record Result(TravelPlan travelPlan, Employee employee, TravelPlanEmployee travelPlanEmployee, Long travelPlanEmployeeId) {
    }

    private static @NonNull AmountResult getAmountBreakdown(List<ExpenseResponseDTO> expenseDTOs) {
        Map<LocalDate, DailyAmountResult> dailyTotals = expenseDTOs.stream()
                .collect(Collectors.toMap(
                        dto -> dto.getCreatedAt().toLocalDate(),
                        dto -> new DailyAmountResult(dto.getAmount(), dto.getEmployee().getMaxAmountPerDay()), // Value mapper: initial summary
                        (DailyAmountResult existingSummary,DailyAmountResult newSummary) -> new DailyAmountResult(
                                existingSummary.totalAmount + newSummary.totalAmount,
                                newSummary.maxAmountPerDay
                        )
                ));

        Integer totalAmount = dailyTotals.values().stream().mapToInt(i->i.totalAmount).sum();
        Integer totalClaimedAmount = dailyTotals.values().stream().mapToInt(i->Math.min(i.totalAmount, i.maxAmountPerDay)).sum();
        Integer totalUnclaimableAmount = dailyTotals.values().stream().mapToInt(i->Math.max(i.totalAmount-i.maxAmountPerDay, 0)).sum();

        AmountResult amountResult = new AmountResult(totalAmount, totalClaimedAmount, totalUnclaimableAmount);
        return amountResult;
    }

    private record AmountResult(Integer totalAmount, Integer totalClaimedAmount, Integer totalUnclaimableAmount) {
    }

    private record DailyAmountResult(Integer totalAmount, Integer maxAmountPerDay) {
    }

    private void validateAccessPermission(TravelPlanEmployee travelPlanEmployee, CustomUserDetails userDetails) {
        boolean isHR = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("HR"));
        
        boolean isOwner = travelPlanEmployee.getEmployee().getEmail().equals(userDetails.getUsername());
        
        if (!isHR && !isOwner) {
            throw new RuntimeException("Unauthorized access to expenses");
        }
    }

    private @NonNull Expense getExpense(Long expenseId) {
        Expense expense = expenseRepo.findById(expenseId).orElse(null);
        if (expense == null) {
            throw new ResourceNotFoundException("Expense with id " + expenseId + " doesn't exist");
        }
        return expense;
    }

    private void validateStatusTransition(String currentStatus, String newStatus) {
        if ("Approved".equals(currentStatus) || "Rejected".equals(currentStatus)) {
            throw new RuntimeException("Cannot change status of " + currentStatus + " expense");
        }

        if ("Draft".equals(currentStatus) && !"Submitted".equals(newStatus)) {
            throw new RuntimeException("Draft expense can only be submitted");
        }

        if ("Submitted".equals(currentStatus) && !("Approved".equals(newStatus) || "Rejected".equals(newStatus))) {
            throw new RuntimeException("Submitted expense can only be approved or rejected");
        }
    }

    private void validateUserRole(String status, CustomUserDetails userDetails) {
        if ("Approved".equals(status) || "Rejected".equals(status)) {
            boolean isHR = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("HR"));
            if (!isHR) {
                throw new RuntimeException("Only HR can approve or reject expenses");
            }
        }
    }

    private void sendSubmissionNotification(Expense expense) {
        try {
            String subject = "New Expense Submitted - " + expense.getTravelPlanEmployee().getEmployee().getName();
            String body = "Employee " + expense.getTravelPlanEmployee().getEmployee().getName() +
                    " has submitted an expense of " + expense.getAmount() +
                    " for travel to " + expense.getTravelPlanEmployee().getTravelPlan().getPlace() +
                    ". Description: " + expense.getDescription();


            com.example.hrmsbackend.dtos.request.EmailDetailsDTO emailDetails = new com.example.hrmsbackend.dtos.request.EmailDetailsDTO();

            Configuration hrEmailConfig = configurationRepo.findByConfigKey("hr_email").orElse(null);
            if (hrEmailConfig != null) {
                emailDetails.setRecipient(hrEmailConfig.getConfigValue());
            }

            emailDetails.setSubject(subject);
            emailDetails.setMsgBody(body);
            emailDetails.setAttachment(null);

            emailService.sendSimpleMail(emailDetails);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send notification email");
        }
    }

    private void updateExpenseStatus(Expense expense, ExpenseStatusRequestDTO dto, CustomUserDetails userDetails) {
        expense.setStatus(dto.getStatus());

        if ("Approved".equals(dto.getStatus())) {
            expense.setApprovedAt(LocalDateTime.now());
        }

        Employee statusChangedBy = employeeRepo.findByEmail(userDetails.getUsername());
        expense.setStatusChangedBy(statusChangedBy);

        if ("Rejected".equals(dto.getStatus()) && dto.getRemarks() != null) {
            expense.setRemarks(dto.getRemarks());
        }
    }

    private @NonNull TravelPlanEmployee getTravelPlanEmployee(Long travelPlanEmployeeId) {
        TravelPlanEmployee travelPlanEmployee = travelPlanEmployeeRepo.findById(travelPlanEmployeeId).orElse(null);
        if (travelPlanEmployee == null) {
            throw new ResourceNotFoundException("Travel plan employee with id " + travelPlanEmployeeId + " doesn't exist");
        }
        return travelPlanEmployee;
    }

    private void validateSubmissionWindow(TravelPlanEmployee travelPlanEmployee) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = travelPlanEmployee.getTravelPlan().getStartDate();
        LocalDate endDate = travelPlanEmployee.getTravelPlan().getEndDate();

        if (today.isBefore(startDate)) {
            throw new RuntimeException("Expense submission is only allowed after trip start date");
        }

        LocalDate submissionDeadline = endDate.plusDays(10);
        if (today.isAfter(submissionDeadline)) {
            throw new RuntimeException("Expense submission is not allowed after 10 days from trip end date");
        }
    }

    private Media uploadProofFile(MultipartFile proofFile, CustomUserDetails userDetails) {
        if (proofFile == null || proofFile.isEmpty()) {
            throw new RuntimeException("Proof file is mandatory");
        }
        return mediaService.upload(proofFile, "expense-proof", userDetails);
    }
}
