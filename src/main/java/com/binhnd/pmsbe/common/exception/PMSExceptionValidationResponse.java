package com.binhnd.pmsbe.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class PMSExceptionValidationResponse {
    private int httpStatusCode;
    private String httpStatusName;
    private String errorCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    private List<PMSExceptionValidationMsg> errorDetails;

    public PMSExceptionValidationResponse(HttpStatus httpStatus, String errorCode, List<PMSExceptionValidationMsg> errorDetails) {
        this.httpStatusCode = httpStatus.value();
        this.httpStatusName = httpStatus.name();
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
        this.errorDetails = errorDetails;
    }
}
