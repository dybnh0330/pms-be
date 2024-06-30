package com.binhnd.pmsbe.dto.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MedicalRecordDetailResponse {

    private Long id;
    private String title;
    private String description;
    private Timestamp createTime;
    private String createBy;
    private Timestamp updateTime;
    private String updateBy;
}
