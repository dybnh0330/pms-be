package com.binhnd.pmsbe.common.constants;

import java.util.List;

public final class PMSExcelConstant {

    public static final String TIMEZONE_VIETNAM = "Asia/Ho_Chi_Minh";
    public static final String TIMES_NEW_ROMAN_FONT = "Times New Roman";
    public static final String FORMAT_DAY_HOUR = "dd/MM/YYYY HH:mm:ss";
    public static final String FORMAT_DAY_HOUR_DOB = "dd/MM/YYYY";

    // CONTAIN HEADER EXCEL
    public static final String DEPARTMENT_CONTAIN_HEADER = "Danh sách khoa bệnh điều trị";
    public static final String STAFF_CONTAIN_HEADER = "Danh sách nhân viên bệnh viên";

    // SHEET NAME EXCEL
    public static final String DEPARTMENT_SHEET_NAME = "department";
    public static final String MEDICAL_STAFF_SHEET_NAME = "medical-staff";


    public static final List<HeaderMapper> DEPARTMENT_HEADER = List.of(
        BaseExcelColumn.ORDER_COLUMN,
        DepartmentColumn.DEPARTMENT_NAME_COLUMN,
        DepartmentColumn.DEPARTMENT_DESCRIPTION_COLUMN,
        BaseExcelColumn.CREATE_TIME,
        BaseExcelColumn.UPDATE_TIME,
        BaseExcelColumn.CREATE_BY,
        BaseExcelColumn.UPDATE_BY
    );

    public enum BaseExcelColumn implements HeaderMapper {
        ORDER_COLUMN("STT", "order"),
        ERROR("error", "error"),

        CREATE_TIME("Thời gian tạo", "createTime"),
        UPDATE_TIME("Thời gian cập nhật", "updateTime"),
        CREATE_BY("Người tạo", "createBy"),
        UPDATE_BY("Người cập nhật", "updateBy");

        private final String headerName;
        private final String field;

        BaseExcelColumn(String name, String field) {
            this.headerName = name;
            this.field = field;
        }

        public String getHeader() {
            return headerName;
        }

        public String getField() {
            return field;
        }
    }

    public enum DepartmentColumn implements HeaderMapper {

        DEPARTMENT_NAME_COLUMN("Tên khoa bệnh", "name"),
        DEPARTMENT_DESCRIPTION_COLUMN("Mô tả khoa bệnh", "description");
        private final String headerName;
        private final String field;

        DepartmentColumn(String name, String field) {
            this.headerName = name;
            this.field = field;
        }

        public String getHeader() {
            return headerName;
        }

        public String getField() {
            return field;
        }
    }

    public enum MedicalStaffColumn implements HeaderMapper {

        NAME_COLUMN("Tên nhân viên", "name"),
        GENDER_COLUMN("Giới tính", "gender"),
        DOB_COLUMN ("Ngày sinh", "dob"),
        ADDRESS_COLUMN ("Địa chỉ", "address"),
        CCCD_COLUMN ("Số CCCD/CMND", "cccd"),
        PHONE_NUMBER_COLUMN ("Số điện thoại", "phoneNumber"),
        EMAIL_COLUMN ("Email", "email"),
        CERTIFICATE_COLUMN ("Số chứng chỉ", "certificate"),
        SPECIALIZE_COLUMN ("Chuyên môn", "specialize"),
        DEPARTMENT_COLUMN ("Khoa bệnh", "department");
        private final String headerName;
        private final String field;

        MedicalStaffColumn(String name, String field) {
            this.headerName = name;
            this.field = field;
        }

        public String getHeader() {
            return headerName;
        }

        public String getField() {
            return field;
        }
    }

    public interface HeaderMapper {
        String getHeader();

        String getField();
    }

}
