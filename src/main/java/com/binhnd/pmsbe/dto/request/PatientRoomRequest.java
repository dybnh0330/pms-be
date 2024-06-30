package com.binhnd.pmsbe.dto.request;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PatientRoomRequest {

    private String roomNumber;
    private Boolean status;
    private Long totalBed;
    private Long departmentId;

}
