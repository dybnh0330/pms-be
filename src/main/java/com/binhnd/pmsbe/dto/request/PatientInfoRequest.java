package com.binhnd.pmsbe.dto.request;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PatientInfoRequest {

    private String patientName;
    private Timestamp dob;
    private Long gender;
    private String bhytCode;
    private String cccdNumber;
    private String address;
    private String patientPhone;
    private String guardianPhone;

}
