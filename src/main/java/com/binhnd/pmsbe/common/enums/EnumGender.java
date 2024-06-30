package com.binhnd.pmsbe.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(type = "integer")
public enum EnumGender {

    MALE(0), FEMALE(1);

    public static final EnumGender[] VALUES = values();
    @JsonValue
    private final Integer value;

    EnumGender(Integer value) {
        this.value = value;
    }

    public static EnumGender resolve(int value) {
        for (EnumGender es : VALUES) {
            if (es.value == value) return es;
        }

        return null;
    }

    public Integer getValue() {
        return value;
    }
}
