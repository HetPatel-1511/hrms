package com.example.hrmsbackend.mappers;

import com.example.hrmsbackend.dtos.request.ConfigurationRequestDTO;
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
        if (employee.getManager()!=null){
            dto.setManager(toEmployeeSummaryDTO(employee.getManager()));
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

    public TravelPlanSummaryResponseDTO toTravelPlanSummaryResponseDTO(TravelPlan travelPlan) {
        if (travelPlan == null) return null;
        TravelPlanSummaryResponseDTO dto = modelMapper.map(travelPlan, TravelPlanSummaryResponseDTO.class);
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

    public TravelPlanResponseDTO toTravelPlanResponseDTOWithoutEmployees(TravelPlan travelPlan) {
        if (travelPlan == null) return null;
        TravelPlanResponseDTO dto = modelMapper.map(travelPlan, TravelPlanResponseDTO.class);
        return dto;
    }

    public List<TravelPlanResponseDTO> toTravelPlanResponseDTOListWithoutEmployees(List<TravelPlan> travelPlans) {
        if (travelPlans == null) return Collections.emptyList();
        return travelPlans.stream().map(this::toTravelPlanResponseDTOWithoutEmployees).toList();
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
        ExpenseResponseDTO dto = modelMapper.map(expense, ExpenseResponseDTO.class);
        dto.setExpenseMedias(expense.getExpenseMedias().stream().map(em->toMediaResponseDTO(em.getExpenseMedias())).toList());
        return dto;
    }

    public EmployeeManagerDetailsResponseDTO toEmployeeManagerDetailsResponseDTO(Employee employee) {
        if (employee == null) return null;
        EmployeeManagerDetailsResponseDTO dto = new EmployeeManagerDetailsResponseDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setDesignation(employee.getDesignation());
        dto.setCreatedAt(employee.getCreatedAt());

        if (employee.getManager() != null) {
            dto.setManager(toEmployeeManagerDetailsResponseDTO(employee.getManager()));
        }

        if (employee.getProfileMedia() != null) {
            dto.setProfileMedia(toMediaResponseDTO(employee.getProfileMedia()));
        }

        return dto;
    }

    public EmployeeDetailsResponseDTO toEmployeeDetailsResponseDTO(Employee employee) {
        if (employee == null) return null;
        EmployeeDetailsResponseDTO dto = new EmployeeDetailsResponseDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setDesignation(employee.getDesignation());
        dto.setCreatedAt(employee.getCreatedAt());

        if (employee.getManager() != null) {
            dto.setManager(toEmployeeManagerDetailsResponseDTO(employee.getManager()));
        }

        if (employee.getProfileMedia() != null) {
            dto.setProfileMedia(toMediaResponseDTO(employee.getProfileMedia()));
        }

        if (employee.getSubordinates() != null) {
            dto.setDirectReports(toEmployeeSummaryDTOList(employee.getSubordinates()));
        }

        return dto;
    }

    public List<ExpenseResponseDTO> toExpenseResponseDTOList(List<Expense> expenses) {
        if (expenses == null) return Collections.emptyList();
        return expenses.stream().map(this::toExpenseResponseDTO).toList();
    }

    public Configuration toConfiguration(ConfigurationRequestDTO configurationRequestDTO) {
        if (configurationRequestDTO == null) return null;
        return modelMapper.map(configurationRequestDTO, Configuration.class);
    }

    public ConfigurationResponseDTO toConfigurationResponseDTO(Configuration configuration) {
        if (configuration == null) return null;
        return modelMapper.map(configuration, ConfigurationResponseDTO.class);
    }

    public List<ConfigurationResponseDTO> toConfigurationResponseDTOList(List<Configuration> configurations) {
        if (configurations == null) return Collections.emptyList();
        return configurations.stream().map(this::toConfigurationResponseDTO).toList();
    }

    public JobOpeningResponseDTO toJobOpeningResponseDTO(JobOpening jobOpening) {
        if (jobOpening == null) return null;
        JobOpeningResponseDTO dto = modelMapper.map(jobOpening, JobOpeningResponseDTO.class);
        if (jobOpening.getHr() != null) {
            dto.setHr(toEmployeeSummaryDTO(jobOpening.getHr()));
        }
        if (jobOpening.getDescriptionMedia() != null) {
            dto.setDescriptionMedia(toMediaResponseDTO(jobOpening.getDescriptionMedia()));
        }
        if (jobOpening.getCvReviewers() != null) {
            dto.setCvReviewers(jobOpening.getCvReviewers().stream()
                    .map(cvReviewer -> toEmployeeSummaryDTO(cvReviewer.getReviewer()))
                    .toList());
        }
        return dto;
    }

    public List<JobOpeningResponseDTO> toJobOpeningResponseDTOList(List<JobOpening> jobOpenings) {
        if (jobOpenings == null) return Collections.emptyList();
        return jobOpenings.stream().map(this::toJobOpeningResponseDTO).toList();
    }

    public ShareJobResponseDTO toShareJobResponseDTO(SharedJob sharedJob) {
        if (sharedJob == null) return null;
        ShareJobResponseDTO dto = modelMapper.map(sharedJob, ShareJobResponseDTO.class);
        if (sharedJob.getSharedBy() != null) {
            dto.setSharedBy(toEmployeeSummaryDTO(sharedJob.getSharedBy()));
        }
        if (sharedJob.getJobOpening() != null) {
            dto.setJobOpeningId(sharedJob.getJobOpening().getId());
        }
        return dto;
    }

    public ReferralResponseDTO toReferralResponseDTO(Referal referal) {
        if (referal == null) return null;
        ReferralResponseDTO dto = modelMapper.map(referal, ReferralResponseDTO.class);
        if (referal.getReferedBy() != null) {
            dto.setReferredBy(toEmployeeSummaryDTO(referal.getReferedBy()));
        }
        if (referal.getJobOpening() != null) {
            dto.setJobOpening(toJobOpeningResponseDTO(referal.getJobOpening()));
        }
        return dto;
    }
}
