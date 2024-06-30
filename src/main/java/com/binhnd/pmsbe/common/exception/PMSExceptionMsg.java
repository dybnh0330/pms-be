package com.binhnd.pmsbe.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PMSExceptionMsg {

    private String key;
    private String messageDefault;
    private List<String> params;

}
