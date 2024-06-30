package com.binhnd.pmsbe.dto.response;

import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Data
public class StaffRoomResponse {

    private Long roomId;
    private String roomNumber;
    private List<MedicalStaffResponse> staffs;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String createBy;
    private String updateBy;

}
