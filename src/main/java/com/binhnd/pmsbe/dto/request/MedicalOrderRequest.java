package com.binhnd.pmsbe.dto.request;

import lombok.Data;

@Data
public class MedicalOrderRequest {
    private Long medicalOrderId;
    private Long categoryId;
    private String note;
    private Long type;
    private String unit;
    private Long quantity;
}
