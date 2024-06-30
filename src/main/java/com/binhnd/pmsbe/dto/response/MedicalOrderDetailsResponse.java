package com.binhnd.pmsbe.dto.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MedicalOrderDetailsResponse {
    private Long id;
    private String orderName;
    private String unit;
    private String note;
    private String type;
    private Long quantity;
    private Timestamp createTime;
    private String createBy;
}
