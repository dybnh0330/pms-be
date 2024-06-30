package com.binhnd.pmsbe.dto.request;

import lombok.Data;

@Data
public class MedicalRecordDetailRequest {
    private Long recordId;
    private String title;
    private String description;

}
