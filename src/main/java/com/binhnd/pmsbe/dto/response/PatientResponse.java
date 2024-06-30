package com.binhnd.pmsbe.dto.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PatientResponse {

    private Long id;
    private String patientCode;
    private String patientName;
    private String address;
    private Long gender;
    private Timestamp dob;
    private String patientPhone;
    private String guardianPhone;
    private String bhytCode;
    private String cccdNumber;
    private Long departmentId;
    private String departmentName;
    private Long roomId;
    private String roomNumber;
    private Long patientBedId;
    private String bedNumber;
    private Long medicalStaffId;
    private String medicalStaffName;
    private Long status;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String createBy;
    private String updateBy;

}
