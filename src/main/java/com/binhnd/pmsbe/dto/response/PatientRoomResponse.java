package com.binhnd.pmsbe.dto.response;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class PatientRoomResponse {

    private Long id;
    private String roomCode;
    private String roomNumber;
    private Long totalBed;
    private Boolean status;
    private Long departmentId;
    private String departmentName;
    private List<Long> bedIds;
    private List<String> bedNumbers;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String createBy;
    private String updateBy;
}
