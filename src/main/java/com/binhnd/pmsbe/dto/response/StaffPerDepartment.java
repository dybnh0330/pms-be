package com.binhnd.pmsbe.dto.response;

import lombok.Data;

@Data
public class StaffPerDepartment {

    private String departmentName;
    private Long totalStaffInDepartment;
}
