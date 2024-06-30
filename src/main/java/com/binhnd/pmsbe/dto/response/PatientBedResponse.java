package com.binhnd.pmsbe.dto.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PatientBedResponse {

    private Long id;
    private String bedCode;
    private String bedNumber;
    private Boolean status;
    private Long roomId;
    private String roomNumber;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String createBy;
    private String updateBy;
}
