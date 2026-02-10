package com.example.hrmsbackend.services;

import com.example.hrmsbackend.dtos.request.CustomUserDetails;
import com.example.hrmsbackend.dtos.request.TravelPlanRequestDTO;
import com.example.hrmsbackend.dtos.request.TravelingEmployeeRequestDTO;
import com.example.hrmsbackend.dtos.response.EmployeeSummaryDTO;
import com.example.hrmsbackend.dtos.response.TravelPlanResponseDTO;
import com.example.hrmsbackend.entities.Employee;
import com.example.hrmsbackend.entities.TravelPlan;
import com.example.hrmsbackend.entities.TravelPlanEmployee;
import com.example.hrmsbackend.exceptions.ResourceNotFoundException;
import com.example.hrmsbackend.mappers.EntityMapper;
import com.example.hrmsbackend.repos.EmployeeRepo;
import com.example.hrmsbackend.repos.TravelPlanEmployeeRepo;
import com.example.hrmsbackend.repos.TravelPlanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TravelPlanService {
    private TravelPlanRepo travelPlanRepo;
    private EmployeeRepo employeeRepo;
    private TravelPlanEmployeeRepo travelPlanEmployeeRepo;
    private EntityMapper entityMapper;

    @Autowired
    public TravelPlanService(TravelPlanRepo travelPlanRepo, EmployeeRepo employeeRepo, TravelPlanEmployeeRepo travelPlanEmployeeRepo, EntityMapper entityMapper) {
        this.travelPlanRepo = travelPlanRepo;
        this.employeeRepo = employeeRepo;
        this.travelPlanEmployeeRepo = travelPlanEmployeeRepo;
        this.entityMapper = entityMapper;
    }

    public List<TravelPlanResponseDTO> getAll() {
        return entityMapper.toTravelPlanResponseDTOList(travelPlanRepo.findAll());
    }

    @Transactional
    public TravelPlanResponseDTO create(TravelPlanRequestDTO dto, CustomUserDetails userDetails) {
        if (dto.getEndDate().isBefore(dto.getStartDate())){
            throw new RuntimeException("Invalid start and end date");
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
}
