package com.example.hrmsbackend.mappers;

import com.example.hrmsbackend.dtos.request.EmployeeRequestDTO;
import com.example.hrmsbackend.dtos.response.*;
import com.example.hrmsbackend.entities.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class EntityMapper {

    private ModelMapper modelMapper;

    @Autowired
    public EntityMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //  Employees
    public Employee toEmployee(EmployeeRequestDTO employee) {
        if (employee == null) return null;
        return modelMapper.map(employee, Employee.class);
    }

    public AuthEmployeeResponseDTO toAuthEmployeeResponseDTO(Employee employee) {
        if (employee == null) return null;
        AuthEmployeeResponseDTO dto = modelMapper.map(employee, AuthEmployeeResponseDTO.class);
        if (employee.getEmployeeRoles()!=null){
            dto.setRoles(employee.getEmployeeRoles().stream().map(this::mapRole).toList());
        }
        return dto;
    }

    public EmployeeSummaryDTO toEmployeeSummaryDTO(Employee employee) {
        if (employee == null) return null;
        return modelMapper.map(employee, EmployeeSummaryDTO.class);
    }

    public List<EmployeeSummaryDTO> toEmployeeSummaryDTOList(List<Employee> employees) {
        if (employees == null) return Collections.emptyList();
        return employees.stream().map(this::toEmployeeSummaryDTO).toList();
    }

    public List<AuthEmployeeResponseDTO> toAuthEmployeeResponseDTOList(List<Employee> employees) {
        if (employees == null) return Collections.emptyList();
        return employees.stream().map(this::toAuthEmployeeResponseDTO).toList();
    }

    //  Roles
    public RoleResponseDTO mapRole(EmployeeRole employeeRole) {
        RoleResponseDTO role = new RoleResponseDTO();
        role.setId(employeeRole.getRole().getId());
        role.setName(employeeRole.getRole().getName());
        return role;
    }

    public List<RoleResponseDTO> toRoleResponseDTOList(List<Role> roles) {
        if (roles == null) return Collections.emptyList();
        return roles.stream().map(this::toRoleResponseDTO).toList();
    }

    public RoleResponseDTO toRoleResponseDTO(Role role) {
        if (role == null) return null;
        return modelMapper.map(role, RoleResponseDTO.class);
    }

    // Travel Plan
    public TravelPlanResponseDTO toTravelPlanResponseDTO(TravelPlan travelPlan) {
        if (travelPlan == null) return null;
        TravelPlanResponseDTO dto = modelMapper.map(travelPlan, TravelPlanResponseDTO.class);
        if (travelPlan.getTravelPlanEmployees() != null) {
            dto.setTravellingEmployees(travelPlan.getTravelPlanEmployees().stream().map(this::mapTravellingEmployees).toList());
        }
        return dto;
    }

    public EmployeeSummaryDTO mapTravellingEmployees(TravelPlanEmployee travelPlanEmployee) {
        EmployeeSummaryDTO dto = this.toEmployeeSummaryDTO(travelPlanEmployee.getEmployee());
        dto.setMaxAmountPerDay(travelPlanEmployee.getMaxAmountPerDay());
        return dto;
    }

    public List<TravelPlanResponseDTO> toTravelPlanResponseDTOList(List<TravelPlan> travelPlans) {
        if (travelPlans == null) return Collections.emptyList();
        return travelPlans.stream().map(this::toTravelPlanResponseDTO).toList();
    }

    public TravelDocumentResponseDTO toTravelDocumentResponseDTO(TravelDocument travelDocument) {
        if (travelDocument == null) return null;
        return modelMapper.map(travelDocument, TravelDocumentResponseDTO.class);
    }

    public List<TravelDocumentResponseDTO> toTravelDocumentResponseDTOList(List<TravelDocument> travelDocuments) {
        if (travelDocuments == null) return Collections.emptyList();
        return travelDocuments.stream().map(this::toTravelDocumentResponseDTO).toList();
    }

    public DocumentTypeResponseDTO toDocumentTypeResponseDTO(DocumentType documentType) {
        if (documentType == null) return null;
        return modelMapper.map(documentType, DocumentTypeResponseDTO.class);
    }

    public List<DocumentTypeResponseDTO> toDocumentTypeResponseDTOList(List<DocumentType> documentTypes) {
        if (documentTypes == null) return Collections.emptyList();
        return documentTypes.stream().map(this::toDocumentTypeResponseDTO).toList();
    }

    public MediaResponseDTO toMediaResponseDTO(Media media) {
        if (media == null) return null;
        return modelMapper.map(media, MediaResponseDTO.class);
    }

    public ExpenseResponseDTO toExpenseResponseDTO(Expense expense) {
        if (expense == null) return null;
        return modelMapper.map(expense, ExpenseResponseDTO.class);
    }

    public List<ExpenseResponseDTO> toExpenseResponseDTOList(List<Expense> expenses) {
        if (expenses == null) return Collections.emptyList();
        return expenses.stream().map(this::toExpenseResponseDTO).toList();
    }
}
