package com.binhnd.pmsbe.dto.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PatientAdmissionResponse {
    private Long id;
    private String patientName;
    private Long gender;
    private Timestamp dob;
    private String address;
    private String medicalHistory;
    private String reason;
    private Timestamp admissionTime;

}
