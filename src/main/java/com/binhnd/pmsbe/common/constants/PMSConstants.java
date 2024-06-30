package com.binhnd.pmsbe.common.constants;

import java.util.Arrays;
import java.util.List;

public final class PMSConstants {

    private PMSConstants() {
    }

    public static final String PREFIX_URL = "/api/pms";

    // DEPARTMENT STATUS
    public static final Long IS_FULL = 0L;
    public static final Long IS_EMPTY = 1L;

    // SOFT DIRECTION
    public static final String ASC = "asc";
    public static final String DESC = "desc";

    //SPECIALIZE STAFF
    public static final String DOCTOR = "Bác sĩ";

    // PATIENT STATUS
    public static final Long HOSPITALIZATION = 0L;
    public static final Long UNDER_TREATMENT = 1L;
    public static final Long LEAVE_HOSPITAL = 2L;

    // RECORD TITLE
    public static final String RECORD_TITLE = "Thông tin bệnh án bệnh nhân mã ";

    // MEDICAL ORDER TITLE
    public static final String ORDER_TITLE = "Thông tin y lệnh điều trị bệnh nhân mã ";

    //MEDICAL ORDER TYPE
    public static final Long MEDICINE_ORDER = 0L;
    public static final Long TEST_ORDER = 1L;
    public static final Long SURGERY_ORDER = 2L;
    public static final List<Long> LIST_TYPE = List.of(TEST_ORDER, MEDICINE_ORDER, SURGERY_ORDER);

    // UNIT, QUANTITY OF SURGERY AND TEST ORDER
    public static final String UNIT = "lần";
    public static final Long QUANTITY = 1L;
    public static final String NOTE = "Đã thực hiện trước khi nhập khoa";

    // DEFAULT PASSWORD
    public static final String DEFAULT_PASSWORD = "123456aA@";

    // ACCOUNT STATUS
    public static final Boolean LOCK_ACCOUNT = Boolean.FALSE;
    public static final Boolean UNLOCK_ACCOUNT = Boolean.TRUE;

    // TOKEN
    public static final String JWT_PREFIX = "Bearer ";
    public static final String APP_USERNAME_TOKEN_FIELD = "username";
    public static final String APP_SCOPE_TOKEN_FIELD = "scope";
    public static final String APP_MEDICAL_STAFF_NAME = "name";
    public static final String APP_PRIVATE_KEY_FILE = "privatekey.pem";

    // ALLOW FILE
    public static final List<String> ALLOWED_FILE_TYPE = Arrays.asList("application/pdf", "image/jpeg", "image/png");

    //GENDER

    public static final String MALE = "Nam";
    public static final String FEMALE = "Nữ";

}
