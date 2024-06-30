package com.binhnd.pmsbe.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class PMSExceptionResponse {
    private Integer httpStatusCode;
    private String httpStatusName;
    private String errorCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private PMSExceptionMsg pmsExceptionMsg;

    public PMSExceptionResponse(HttpStatus httpStatus, String errorCode, PMSExceptionMsg pmsExceptionMsg) {
        this.httpStatusCode = httpStatus.value();
        this.httpStatusName = httpStatus.name();
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
        this.pmsExceptionMsg = pmsExceptionMsg;
    }
}

