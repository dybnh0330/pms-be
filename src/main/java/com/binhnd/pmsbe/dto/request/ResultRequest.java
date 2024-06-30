package com.binhnd.pmsbe.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ResultRequest {

    private Long recordId;
    private MultipartFile[] files;

}
