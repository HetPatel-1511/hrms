package com.example.hrmsbackend.services;

import com.example.hrmsbackend.dtos.request.CustomUserDetails;
import com.example.hrmsbackend.dtos.request.ExpenseCreateRequestDTO;
import com.example.hrmsbackend.dtos.request.ExpenseStatusRequestDTO;
import com.example.hrmsbackend.dtos.response.ExpenseListResponseDTO;
import com.example.hrmsbackend.dtos.response.ExpenseResponseDTO;
import com.example.hrmsbackend.entities.*;
import com.example.hrmsbackend.exceptions.ResourceNotFoundException;
import com.example.hrmsbackend.mappers.EntityMapper;
import com.example.hrmsbackend.repos.EmployeeRepo;
import com.example.hrmsbackend.repos.ExpenseRepo;
import com.example.hrmsbackend.repos.TravelPlanEmployeeRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpenseService {
    private ExpenseRepo expenseRepo;
    private TravelPlanEmployeeRepo travelPlanEmployeeRepo;
    private MediaService mediaService;
    private EntityMapper entityMapper;
    private EmailService emailService;
    private EmployeeRepo employeeRepo;

    @Value("${hr.email}")
    private String hrEmail;

    @Autowired
    public ExpenseService(ExpenseRepo expenseRepo, TravelPlanEmployeeRepo travelPlanEmployeeRepo, MediaService mediaService, EntityMapper entityMapper, EmailService emailService, EmployeeRepo employeeRepo) {
        this.expenseRepo = expenseRepo;
        this.travelPlanEmployeeRepo = travelPlanEmployeeRepo;
        this.mediaService = mediaService;
        this.entityMapper = entityMapper;
        this.emailService = emailService;
        this.employeeRepo = employeeRepo;
    }

    @Transactional
    public ExpenseResponseDTO create(ExpenseCreateRequestDTO dto, CustomUserDetails userDetails) {
        TravelPlanEmployee travelPlanEmployee = getTravelPlanEmployee(dto.getTravelPlanEmployeeId());
        validateSubmissionWindow(travelPlanEmployee);

        Media media = uploadProofFile(dto.getExpenseMedia(), userDetails);

        Expense expense = addExpense(dto, travelPlanEmployee, media);
        expenseRepo.save(expense);

        return entityMapper.toExpenseResponseDTO(expense);
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

    public ExpenseListResponseDTO getExpensesByTravelPlanEmployeeId(Long travelPlanEmployeeId, CustomUserDetails userDetails) {
        TravelPlanEmployee travelPlanEmployee = getTravelPlanEmployee(travelPlanEmployeeId);
        validateAccessPermission(travelPlanEmployee, userDetails);

        List<Expense> expenses = expenseRepo.findByTravelPlanEmployeeId(travelPlanEmployeeId);
        List<ExpenseResponseDTO> expenseDTOs = entityMapper.toExpenseResponseDTOList(expenses);

        AmountResult amountResult = getAmountBreakdown(expenses);

        ExpenseListResponseDTO response = new ExpenseListResponseDTO();
        response.setExpenses(expenseDTOs);
        response.setTotalClaimedAmount(amountResult.totalClaimedAmount());
        response.setTotalUnclaimableAmount(amountResult.totalUnclaimableAmount());
        response.setTotalCount((long) expenseDTOs.size());

        return response;
    }

    private static @NonNull AmountResult getAmountBreakdown(List<Expense> expenses) {
        Integer totalClaimedAmount = expenses.stream()
                .mapToInt(expense->Math.min(expense.getAmount(), expense.getTravelPlanEmployee().getMaxAmountPerDay()))
                .sum();

        Integer totalUnclaimableAmount = expenses.stream()
                .mapToInt(expense->Math.max(expense.getAmount() - expense.getTravelPlanEmployee().getMaxAmountPerDay(), 0))
                .sum();
        AmountResult amountResult = new AmountResult(totalClaimedAmount, totalUnclaimableAmount);
        return amountResult;
    }

    private record AmountResult(Integer totalClaimedAmount, Integer totalUnclaimableAmount) {
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
            emailDetails.setRecipient(hrEmail);
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

    private @NonNull Expense addExpense(ExpenseCreateRequestDTO dto, TravelPlanEmployee travelPlanEmployee, Media media) {
        Expense expense = new Expense();
        expense.setTravelPlanEmployee(travelPlanEmployee);
        expense.setExpenseMedia(media);
        expense.setAmount(dto.getAmount());
        expense.setDescription(dto.getDescription());
        expense.setStatus("Draft");
        expense.setCreatedAt(LocalDateTime.now());
        return expense;
    }

}
