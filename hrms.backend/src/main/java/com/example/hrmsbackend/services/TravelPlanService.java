package com.example.hrmsbackend.services;

import com.example.hrmsbackend.dtos.request.CustomUserDetails;
import com.example.hrmsbackend.dtos.request.TravelPlanRequestDTO;
import com.example.hrmsbackend.dtos.request.TravelingEmployeeRequestDTO;
import com.example.hrmsbackend.dtos.response.*;
import com.example.hrmsbackend.entities.*;
import com.example.hrmsbackend.exceptions.ResourceNotFoundException;
import com.example.hrmsbackend.mappers.EntityMapper;
import com.example.hrmsbackend.repos.*;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TravelPlanService {
    private TravelPlanRepo travelPlanRepo;
    private EmployeeRepo employeeRepo;
    private TravelPlanEmployeeRepo travelPlanEmployeeRepo;
    private EntityMapper entityMapper;
    private DocumentTypeRepo documentTypeRepo;
    private MediaService mediaService;
    private TravelDocumentRepo travelDocumentRepo;

    @Autowired
    public TravelPlanService(TravelPlanRepo travelPlanRepo, EmployeeRepo employeeRepo, TravelPlanEmployeeRepo travelPlanEmployeeRepo, EntityMapper entityMapper, DocumentTypeRepo documentTypeRepo, MediaService mediaService, TravelDocumentRepo travelDocumentRepo) {
        this.travelPlanRepo = travelPlanRepo;
        this.employeeRepo = employeeRepo;
        this.travelPlanEmployeeRepo = travelPlanEmployeeRepo;
        this.entityMapper = entityMapper;
        this.documentTypeRepo = documentTypeRepo;
        this.mediaService = mediaService;
        this.travelDocumentRepo = travelDocumentRepo;
    }

    public List<TravelPlanResponseDTO> getAll() {
        return entityMapper.toTravelPlanResponseDTOList(travelPlanRepo.findAllByOrderByCreatedAtDesc());
    }

    @Transactional
    public TravelPlanResponseDTO create(TravelPlanRequestDTO dto, CustomUserDetails userDetails) {
        if (dto.getEndDate().isBefore(dto.getStartDate())){
            throw new RuntimeException("Invalid start and end date");
        }

        if (dto.getStartDate().isBefore(LocalDate.now())){
            throw new RuntimeException("Start date should be in the future.");
        }

        TravelPlan travelPlan = new TravelPlan();
        travelPlan.setPlace(dto.getPlace());
        travelPlan.setPurpose(dto.getPurpose());
        travelPlan.setStartDate(dto.getStartDate());
        travelPlan.setEndDate(dto.getEndDate());

        Employee employee = employeeRepo.findByEmail(userDetails.getUsername());
        travelPlan.setCreatedBy(employee);

        travelPlanRepo.save(travelPlan);

        List<EmployeeSummaryDTO> employeeSummaryDTOs = saveTravelPlanEmployees(dto.getEmployees(), travelPlan);

        TravelPlanResponseDTO res = entityMapper.toTravelPlanResponseDTO(travelPlan);
        res.setTravellingEmployees(employeeSummaryDTOs);
        return res;
    }

    public TravelPlanResponseDTO getById(Long id) {
        TravelPlan travelPlan = travelPlanRepo.findById(id).orElse(null);
        if (travelPlan == null) {
            throw new ResourceNotFoundException("Travel plan with id " + id + " doesn't exist");
        }
        return entityMapper.toTravelPlanResponseDTO(travelPlan);
    }

    public TravelPlanResponseDTO update(Long id, TravelPlanRequestDTO dto) {
        TravelPlan travelPlan = travelPlanRepo.findById(id).orElse(null);
        if (travelPlan == null) {
            throw new ResourceNotFoundException("Travel plan with id " + id + " doesn't exist");
        }
        return entityMapper.toTravelPlanResponseDTO(travelPlan);
    }

    private List<EmployeeSummaryDTO> saveTravelPlanEmployees(List<TravelingEmployeeRequestDTO> employees, TravelPlan travelPlan){
        List<EmployeeSummaryDTO> employeeSummaryDTOs = new ArrayList<>();
        for (TravelingEmployeeRequestDTO travelingEmployeeRequestDTO : employees) {
            Employee emp = employeeRepo.findById(travelingEmployeeRequestDTO.getId()).orElse(null);
            if (emp == null) {
                continue;
            }

            if (travelingEmployeeRequestDTO.getMaxAmountPerDay()<0) {
                throw new RuntimeException("Amount cannot be negative");
            }

            TravelPlanEmployee travelPlanEmployee = new TravelPlanEmployee();

            travelPlanEmployee.setTravelPlan(travelPlan);
            travelPlanEmployee.setEmployee(emp);
            travelPlanEmployee.setMaxAmountPerDay(travelingEmployeeRequestDTO.getMaxAmountPerDay());

            travelPlanEmployeeRepo.save(travelPlanEmployee);
            EmployeeSummaryDTO employeeSummaryDTO = entityMapper.toEmployeeSummaryDTO(travelPlanEmployee.getEmployee());
            employeeSummaryDTOs.add(employeeSummaryDTO);
        }
        return employeeSummaryDTOs;
    }

    public TravelDocumentResponseDTO uploadDocument(Long travelPlanId, Long employeeId, Long documentTypeId, MultipartFile file, String ownerType, CustomUserDetails userDetails) {
        TravelPlanEmployee travelPlanEmployee = getTravelPlanEmployee(travelPlanId, employeeId);
        DocumentType documentType = getDocumentType(documentTypeId);

        Media media = mediaService.upload(file, documentType.getName(), userDetails);

        TravelDocument travelDocument = getTravelDocument(ownerType, documentType, travelPlanEmployee, media);
        travelDocumentRepo.save(travelDocument);

        return entityMapper.toTravelDocumentResponseDTO(travelDocument);
    }

    private static @NonNull TravelDocument getTravelDocument(String ownerType, DocumentType documentType, TravelPlanEmployee travelPlanEmployee, Media media) {
        TravelDocument travelDocument = new TravelDocument();

        travelDocument.setDocumentType(documentType);
        travelDocument.setTravelPlanEmployee(travelPlanEmployee);
        travelDocument.setMedia(media);
        travelDocument.setOwnerType(ownerType);

        return travelDocument;
    }

    private @NonNull DocumentType getDocumentType(Long id) {
        DocumentType documentType = documentTypeRepo.findById(id).orElse(null);
        if (documentType == null) {
            throw new ResourceNotFoundException("Document with id " + id + " doesn't exist");
        }
        return documentType;
    }

    private @Nullable TravelPlanEmployee getTravelPlanEmployee(Long travelPlanId, Long employeeId) {
        TravelPlanEmployee travelPlanEmployee = new TravelPlanEmployee();
        travelPlanEmployee.setEmployee(employeeRepo.findById(employeeId).orElse(null));
        travelPlanEmployee.setTravelPlan(travelPlanRepo.findById(travelPlanId).orElse(null));

        TravelPlanEmployee travelPlanEmployee1 = travelPlanEmployeeRepo.findByTravelPlanAndEmployee(travelPlanEmployee.getTravelPlan(), travelPlanEmployee.getEmployee()).orElse(null);
        if (travelPlanEmployee1 == null) {
            throw new ResourceNotFoundException("Travel plan doesn't exist");
        }
        return travelPlanEmployee1;
    }

    public TravelDocumentListResponseDTO getDocuments(Long travelPlanId, Long employeeId) {
        TravelPlanEmployee travelPlanEmployee = getTravelPlanEmployee(travelPlanId, employeeId);

        Employee employee = getEmployee(employeeId);
        TravelPlan travelPlan = getTravelPlan(travelPlanId);
        List<DocumentType> documentTypes = documentTypeRepo.findAll();

        TravelDocumentListResponseDTO travelDocumentListResponseDTO = getTravelDocumentListResponseDTO(travelPlanEmployee, employee, travelPlan, documentTypes);
        return travelDocumentListResponseDTO;
    }

    private @NonNull TravelDocumentListResponseDTO getTravelDocumentListResponseDTO(TravelPlanEmployee travelPlanEmployee, Employee employee, TravelPlan travelPlan, List<DocumentType> documentTypes) {
        List<TravelDocument> travelDocuments = travelDocumentRepo.findAllByTravelPlanEmployeeId(travelPlanEmployee.getId());
        TravelDocumentListResponseDTO travelDocumentListResponseDTO = new TravelDocumentListResponseDTO();
        travelDocumentListResponseDTO.setTravelDocuments(entityMapper.toTravelDocumentResponseDTOList(travelDocuments));
        travelDocumentListResponseDTO.setEmployee(entityMapper.toEmployeeSummaryDTO(employee));
        travelDocumentListResponseDTO.setTravelPlan(entityMapper.toTravelPlanSummaryResponseDTO(travelPlan));
        travelDocumentListResponseDTO.setDocumentTypes(entityMapper.toDocumentTypeResponseDTOList(documentTypes));
        return travelDocumentListResponseDTO;
    }

    private @NonNull TravelPlan getTravelPlan(Long travelPlanId) {
        TravelPlan travelPlan = travelPlanRepo.findById(travelPlanId).orElse(null);
        if (travelPlan == null) {
            throw new ResourceNotFoundException("Employee not found");
        }
        return travelPlan;
    }

    public MyTravelsResponseDTO getMyTravels(CustomUserDetails userDetails) {
        Employee employee = employeeRepo.findByEmail(userDetails.getUsername());
        if (employee == null) {
            throw new ResourceNotFoundException("User not found");
        }
        
        List<TravelPlan> travelPlans = travelPlanRepo.findByEmployeeOrderByStartDateDesc(employee);
        
        MyTravelsResponseDTO response = new MyTravelsResponseDTO();
        response.setEmployee(entityMapper.toEmployeeSummaryDTO(employee));
        response.setTravelPlans(entityMapper.toTravelPlanResponseDTOListWithoutEmployees(travelPlans));
        return response;
    }

    private @NonNull Employee getEmployee(Long employeeId) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found");
        }
        return employee;
    }
}
