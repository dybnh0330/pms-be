package com.binhnd.pmsbe.dto.request;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ReceivePatientRequest {
    private String bhytCode;
    private String cccdNumber;
    private Timestamp dob;
    private Long gender;
    private String guardianPhone;
    private String reason;
    private String medicalHistory;
    private String patientName;
    private String patientPhone;
    private String address;
}
