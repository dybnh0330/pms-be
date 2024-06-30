package com.binhnd.pmsbe.dto.response;

import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;

@Data
public class DepartmentResponse {

    private Long id;
    private String name;
    private String description;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String createBy;
    private String updateBy;

}
