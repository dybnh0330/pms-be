package com.binhnd.pmsbe.dto.response;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class AccountResponse {

    private Long id;
    private String username;
    private Boolean status;
    private List<Long> roleIds;
    private List<String> roleNames;
    private Long medicalStaffId;
    private String medicalStaffName;
    private Timestamp createTime;
    private String createBy;
    private Timestamp updateTime;
    private String updateBy;

}
