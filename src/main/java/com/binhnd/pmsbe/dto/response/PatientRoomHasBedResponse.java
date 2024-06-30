package com.binhnd.pmsbe.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PatientRoomHasBedResponse {

    private Long id;
    private String roomNumber;
    private Long totalBed;
    private List<PatientBedResponse> patientBeds;
    private Boolean status;
}
