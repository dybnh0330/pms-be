package com.binhnd.pmsbe.dto.request;

import lombok.Data;

@Data
public class PatientInHospitalRequest {

    private Long medicalStaffId;
    private Long departmentId;
    private Long roomId;
    private Long patientBedId;
}
