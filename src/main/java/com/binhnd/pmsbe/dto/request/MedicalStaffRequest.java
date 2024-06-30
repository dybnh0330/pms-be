package com.binhnd.pmsbe.dto.request;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MedicalStaffRequest {
    private String name;
    private Long gender;
    private Timestamp dob;
    private String address;
    private String cccd;
    private String phoneNumber;
    private String email;
    private String certificate;
    private String specialize;
    private Long departmentId;
}
