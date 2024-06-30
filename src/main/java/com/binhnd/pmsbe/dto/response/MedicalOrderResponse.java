package com.binhnd.pmsbe.dto.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MedicalOrderResponse {

    private Long id;
    private String title;
    private Timestamp createTime;
    private Long patientId;
    private String patientName;
}
