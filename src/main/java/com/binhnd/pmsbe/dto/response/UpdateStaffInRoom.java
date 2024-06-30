package com.binhnd.pmsbe.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UpdateStaffInRoom {

    private Long roomId;
    private String roomNumber;
    private Long departmentId;
    private List<Long> medicalStaffIds;
    private List<String> medicalStaffNames;

}
