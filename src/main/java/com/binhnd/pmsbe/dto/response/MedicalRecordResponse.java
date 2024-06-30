package com.binhnd.pmsbe.dto.response;

import lombok.Data;

@Data
public class MedicalRecordResponse {

    private Long recordId;
    private String recordCode;
    private Long patientId;
    private String patientName;
    private String reason;
    private String medicalHistory;
    private String diagnostic;

}
