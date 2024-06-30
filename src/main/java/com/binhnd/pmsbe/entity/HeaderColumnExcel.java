package com.binhnd.pmsbe.entity;

import com.binhnd.pmsbe.common.enums.EnumAlignExcel;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;

import java.util.Set;

@Data
@Builder
@Generated
public class HeaderColumnExcel {
    private String columnName;
    private EnumAlignExcel align;
    private Long widthColumn;
    private Set<String> dataValidationSet;
    private String colValidation;
    private boolean isError = false;
}
