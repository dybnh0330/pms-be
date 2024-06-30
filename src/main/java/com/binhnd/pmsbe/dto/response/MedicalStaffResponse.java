package com.binhnd.pmsbe.dto.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MedicalStaffResponse {

    private Long id;
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
    private String departmentName;
    private String username;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String createBy;
    private String updateBy;
}
