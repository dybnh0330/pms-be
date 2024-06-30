package com.binhnd.pmsbe.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class DashboardResponse {

    private Long countPatient;
    private Long countMedicalStaff;
    private Long countDepartment;
    private Long countAccount;
    private Long patientBeingTreated;
    private Long patientBeenDischarged;
    private Double percentBeingTreated;
    private Double percentBeenDischarged;
    private List<StaffPerDepartment> staffPerDepartments;
}
