package com.binhnd.pmsbe.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class PatientRoomHasBedRequest {
    private String roomNumber;
    private Long totalBed;
    private List<BedNumber> patientBeds;
    private Boolean status;
    private Long departmentId;
}
