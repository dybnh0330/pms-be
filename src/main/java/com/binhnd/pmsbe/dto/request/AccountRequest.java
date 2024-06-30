package com.binhnd.pmsbe.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class AccountRequest {

    private String username;
    private List<Long> roleIds;
    private Long medicalStaffId;
    private Boolean status;
}
