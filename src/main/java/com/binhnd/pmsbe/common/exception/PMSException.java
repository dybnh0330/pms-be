package com.binhnd.pmsbe.common.exception;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PMSException extends RuntimeException {
    
    private EnumPMSException enumPMSException;
    private List<String> msgParams;
    private Exception ex;

    public PMSException(EnumPMSException enumPMSException, List<String> msgParams) {
        super(enumPMSException.getErrorCode());
        this.enumPMSException = enumPMSException;
        this.msgParams = msgParams;
    }

    public PMSException(EnumPMSException enumPMSException) {
        super(enumPMSException.getErrorCode());
        this.enumPMSException = enumPMSException;
        this.msgParams = List.of(enumPMSException.getErrorCode());
    }

    public PMSException(EnumPMSException enumPMSException, Exception exception) {
        super(String.format("%s\n%s", enumPMSException.getErrorCode(), exception.getMessage()));
        this.enumPMSException = enumPMSException;
        this.msgParams = List.of(enumPMSException.getErrorCode());
        this.ex = exception;
    }
}
