package com.example.hrmsbackend.dtos.response;

import java.util.List;

public class TravelDocumentListResponseDTO {
    private List<TravelDocumentResponseDTO> travelDocuments;
    private EmployeeSummaryDTO employee;
    private TravelPlanSummaryResponseDTO travelPlan;
    private List<DocumentTypeResponseDTO> documentTypes;

    public List<TravelDocumentResponseDTO> getTravelDocuments() {
        return travelDocuments;
    }

    public void setTravelDocuments(List<TravelDocumentResponseDTO> travelDocuments) {
        this.travelDocuments = travelDocuments;
    }

    public EmployeeSummaryDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeSummaryDTO employee) {
        this.employee = employee;
    }

    public TravelPlanSummaryResponseDTO getTravelPlan() {
        return travelPlan;
    }

    public void setTravelPlan(TravelPlanSummaryResponseDTO travelPlan) {
        this.travelPlan = travelPlan;
    }

    public List<DocumentTypeResponseDTO> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(List<DocumentTypeResponseDTO> documentTypes) {
        this.documentTypes = documentTypes;
    }
}
