package com.binhnd.pmsbe.dto.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ResultResponse {

    private Long id;
    private String fileName;
    private String fileType;
    private String fileDownloadUri;
    private Timestamp createTime;
    private String createBy;

}
