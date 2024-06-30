package com.binhnd.pmsbe.dto.request;

import lombok.Data;

@Data
public class PatientBedRequest {
    private String bedNumber;
    private Boolean status;
    private Long roomId;
}
