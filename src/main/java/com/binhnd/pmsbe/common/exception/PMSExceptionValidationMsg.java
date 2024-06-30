package com.binhnd.pmsbe.common.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PMSExceptionValidationMsg {
    private String objectName;
    private String field;
    private String messageDefault;
}
