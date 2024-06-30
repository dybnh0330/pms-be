package com.binhnd.pmsbe.common.constants;

import java.util.Arrays;
import java.util.List;

public final class RoleName {

    private RoleName() {}

    public static final String ROLE_ADMIN = "Quản trị viên";
    public static final String ROLE_SPECIALIST = "Bác sĩ chuyên khoa";
    public static final String ROLE_EXAM_DOCTOR = "Bác sĩ khám bệnh";
    public static final String ROLE_MEDICAL_STAFF = "Nhân viên hành chính";
    public static final String ROLE_NURSE = "Điều dưỡng/Y tá";

    public static final List<String> PMS_ROLE = Arrays.asList(ROLE_ADMIN, ROLE_SPECIALIST, ROLE_EXAM_DOCTOR, ROLE_MEDICAL_STAFF, ROLE_NURSE);

}
