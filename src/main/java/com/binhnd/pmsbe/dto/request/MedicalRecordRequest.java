package com.binhnd.pmsbe.dto.request;

import lombok.Data;

@Data
public class MedicalRecordRequest {
    private String reason;
    private String medicalHistory;
    private String diagnostic;

}
