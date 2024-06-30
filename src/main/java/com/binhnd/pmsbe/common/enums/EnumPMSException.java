package com.binhnd.pmsbe.common.enums;

import org.springframework.http.HttpStatus;

public enum EnumPMSException {

    ID_NOT_EQUALS("ID_NOT_EQUALS", HttpStatus.BAD_REQUEST, "object.not-equals", "id không bằng nhau"),

    // DEPARTMENT EXCEPTION
    DEPARTMENT_EXISTED("DEPARTMENT_EXISTED", HttpStatus.BAD_REQUEST, "department.existed", "Thông tin khoa bệnh đã tồn tại"),
    DEPARTMENT_NOT_EXIST("DEPARTMENT_NOT_EXIST", HttpStatus.NOT_FOUND, "department.not-exist", "Thông tin dữ liệu khoa bệnh không tồn tại"),
    DEPARTMENT_INVALID("DEPARTMENT_INVALID", HttpStatus.BAD_REQUEST, "department.data-invalid", "Thông tin dữ liệu khoa bệnh không hợp lệ"),

    // CATEGORY EXCEPTION
    PARENT_CATEGORY_NOT_EXISTED("PARENT_CATEGORY_NOT_EXISTED", HttpStatus.NOT_FOUND, "category.not-exist", "Thông tin danh mục cha không tồn tại"),
    CATEGORY_INVALID("CATEGORY_INVALID", HttpStatus.BAD_REQUEST, "category.data-invalid", "Dữ liệu danh mục không hợp lệ"),
    CATEGORY_NOT_EXISTED("CATEGORY_NOT_EXISTED", HttpStatus.NOT_FOUND, "category.not-found", "Thông tin danh mục không tồn tại"),
    CATEGORY_EXISTED("CATEGORY_EXISTED", HttpStatus.BAD_REQUEST, "category.existed", "Thông tin danh mục đã tồn tại"),
    CATEGORY_CANNOT_DELETED("CATEGORY_CANNOT_DELETED", HttpStatus.BAD_REQUEST, "category.cannot-deleted", "Danh mục này chứa thông tin danh mục khác không thể xoá"),
    ID_EQUALS("ID_EQUALS", HttpStatus.BAD_REQUEST, "category.parentId_equals_id", "Id và parentId không thể bằng nhau"),

    // MEDICAL_STAFF EXCEPTION
    MEDICAL_STAFF_HAD_ACCOUNT("MEDICAL_STAFF_HAD_ACCOUNT", HttpStatus.BAD_REQUEST, "medical-staff.duplicate", "Nhân viên y tế đã được gán tài khoản, vui lòng kiểm tra lại"),
    STAFF_HAS_ACCOUNT("STAFF_HAS_ACCOUNT", HttpStatus.BAD_REQUEST, "medical-staff.delete-fail", "Nhân viên đã có tài khoản không thể bị xoá, vui lòng kiểm tra lại"),
    DELETE_MEDICAL_STAFF_FAIL("DELETE_MEDICAL_STAFF_FAIL", HttpStatus.BAD_REQUEST, "medical-staff.delete-fail", "Bác sĩ đang phụ trách bệnh nhân không thể bị xoá"),
    MEDICAL_STAFF_INVALID("MEDICAL_STAFF_INVALID", HttpStatus.BAD_REQUEST, "medical-staff.data-invalid", "Dữ liệu nhân viên người dùng nhập không hợp lệ"),
    MEDICAL_STAFF_NOT_EXISTED("MEDICAL_STAFF_NOT_EXISTED", HttpStatus.NOT_FOUND, "medical-staff.not-exist", "Dữ liệu nhân viên không tồn tại"),
    MEDICAL_STAFF_EXISTED("MEDICAL_STAFF_EXISTED", HttpStatus.BAD_REQUEST, "medical-staff.existed", "Số CCCD/CMND, SĐT, Email hoặc mã chứng chỉ đã tồn tại"),

    // PATIENT_ROOM_EXCEPTION
    PATIENT_ROOM_DATA_INVALID("PATIENT_ROOM_DATA_INVALID", HttpStatus.BAD_REQUEST, "patient-room.data-invalid", "Dữ liệu buồng bệnh người dùng nhập không hợp lệ"),
    PATIENT_ROOM_NOT_EXISTED("PATIENT_ROOM_NOT_EXISTED", HttpStatus.NOT_FOUND, "patient-room.not-existed", "Dữ liệu buồng bệnh không tồn tại"),
    UPDATE_PATIENT_ROOM_FAIL("UPDATE_PATIENT_ROOM_FAIL", HttpStatus.BAD_REQUEST, "patient-room.update-fail", "Tổng số giường không thể bé hơn số giường hiện tại của phòng"),
    PATIENT_ROOM_EXISTED("PATIENT_ROOM_EXISTED", HttpStatus.BAD_REQUEST, "patient-room.existed", "Buồng bệnh đã tồn tại trong khoa"),
    DELETE_PATIENT_ROOM_FAIL("DELETE_PATIENT_ROOM_FAIL", HttpStatus.BAD_REQUEST, "patient-room.delete-fail", "Buồng bệnh có giường bệnh không thể xoá"),

    // PATIENT BED EXCEPTION
    PATIENT_BED_INVALID_DATA("PATIENT_BED_INVALID_DATA", HttpStatus.BAD_REQUEST, "patient-bed.invalid-data", "Dữ liệu giường bệnh người dùng nhập không hợp lệ"),
    PATIENT_BED_NOT_EXISTED("PATIENT_BED_NOT_EXISTED", HttpStatus.NOT_FOUND, "patient-bed.not-existed", "Thông tin giường bệnh không tồn tại"),
    PATIENT_BED_NOT_IN_ROOM("PATIENT_BED_NOT_IN_ROOM", HttpStatus.BAD_REQUEST, "patient-bed.not-in-room", "Giường bệnh không tồn tại trong buồng bệnh."),
    CREATE_PATIENT_BED_FAIL("CREATE_PATIENT_BED_FAIL", HttpStatus.BAD_REQUEST, "patient-bed.create-fail", "Buồng bệnh đã đủ giường không thể tạo thêm giường bệnh"),
    PATIENT_BED_IN_ROOM_NOT_EXISTED("PATIENT_BED_IN_ROOM_NOT_EXISTED", HttpStatus.BAD_REQUEST, "patient-bed.delete-fail", "Buồng bệnh không có giường không thể thực hiện xoá tất cả"),
    PATIENT_BED_EXISTED("PATIENT_BED_EXISTED", HttpStatus.BAD_REQUEST, "patient-bed.existed", "Dữ liệu giường bệnh đã tồn tại"),

    // STAFF ROOM EXCEPTION
    STAFF_NOT_IN_DEPARTMENT("STAFF_NOT_IN_DEPARTMENT", HttpStatus.BAD_REQUEST, "staff-room.not-in-department", "Có Nhân viên không thuộc khoa bệnh, kiểm tra lại danh sách nhân viên"),

    // RECEIVE PATIENT EXCEPTION
    RECEIVE_PATIENT_DATA_INVALID("RECEIVE_PATIENT_DATA_INVALID", HttpStatus.BAD_REQUEST, "receive-patient.data-invalid", "Dữ liệu thông tin tiếp nhận bệnh nhân không hợp lệ"),

    // PATIENT EXCEPTION
    PATIENT_EXISTED("PATIENT_EXISTED", HttpStatus.BAD_REQUEST, "patient.existed", "Mã BHYT, số CCCD/CMND hoặc SĐT bệnh nhân đã tồn tại, vui lòng kiểm tra lại"),
    PATIENT_NOT_EXIST("PATIENT_NOT_EXIST", HttpStatus.NOT_FOUND, "patient.not-existed", "Thông tin bệnh nhân không tồn tại"),
    PATIENT_DATA_INVALID("PATIENT_DATA_INVALID", HttpStatus.BAD_REQUEST, "patient.data-invalid", "Thông tin dữ liệu bệnh nhân không hợp lệ"),
    UPDATE_INFO_FAIL("UPDATE_INFO_FAIL", HttpStatus.BAD_REQUEST, "patient.update-info-fail", "Bệnh nhân chưa nhập khoa không thể cập nhật thông tin"),
    UPDATE_BED_FAIL("UPDATE_BED_FAIL", HttpStatus.BAD_REQUEST, "patient.update-bed-fail", "Giường bệnh không thuộc phòng bệnh, Vui lòng kiểm tra lại"),
    UPDATE_ROOM_FAIL("UPDATE_ROOM_FAIL", HttpStatus.BAD_REQUEST, "patient.update-room-fail", "Buồng bệnh không thuộc khoa bệnh, vui lòng kiểm tra lại"),
    UPDATE_MEDICAL_STAFF_FAIL("UPDATE_MEDICAL_STAFF_FAIL", HttpStatus.BAD_REQUEST, "patient.update-staff.fail", "Bác sĩ điều trị không thuộc khoa bệnh, vui lòng kiểm tra lại"),
    BED_NOT_EMPTY("BED_NOT_EMPTY", HttpStatus.BAD_REQUEST, "patient.bed-not-empty", "Giường bệnh đã có bệnh nhân, vui lòng kiểm tra lại"),
    MEDICAL_STAFF_NOT_DOCTOR("MEDICAL_STAFF_NOT_DOCTOR", HttpStatus.BAD_REQUEST,"patient.medical-staff-not-doctor", "Nhân viên y tế không phải chuyên môn bác sĩ điều trị"),
    ROOM_NOT_EMPTY("ROOM_NOT_EMPTY", HttpStatus.BAD_REQUEST, "patient.room-not-empty", "Buồng bệnh đã hết giường, vui lòng kiểm tra lại"),

    // MEDICAL ORDER EXCEPTION
    MEDICAL_ORDER_DATA_INVALID("MEDICAL_ORDER_DATA_INVALID", HttpStatus.BAD_REQUEST, "medical-order.data-invalid", "Thông tin y lệnh người dùng nhập không hợp lệ"),
    MEDICAL_ORDER_NOT_EXISTED("MEDICAL_ORDER_NOT_EXISTED", HttpStatus.NOT_FOUND, "medical-order.not-existed", "Thông tin y lệnh điều trị không tồn tại"),
    MEDICAL_ORDER_TYPE_NOT_FOUND("MEDICAL_ORDER_TYPE_NOT_FOUND", HttpStatus.NOT_FOUND, "medical-order.not-found", "Loại y lệnh người dùng nhập không tồn tại"),

    // MEDICAL RECORD EXCEPTION
    MEDICAL_RECORD_DATA_INVALID("MEDICAL_RECORD_DATA_INVALID", HttpStatus.BAD_REQUEST, "medical-record.data-invalid", "Thông tin dữ liệu bệnh án không hợp lệ, vui lòng kiểm tra lại"),
    MEDICAL_RECORD_NOT_EXISTED("MEDICAL_RECORD_NOT_EXISTED", HttpStatus.NOT_FOUND, "medical-record.not-found", "Thông tin bệnh án không tồn tại"),
    MEDICAL_RECORD_INCORRECT("MEDICAL_RECORD_INCORRECT", HttpStatus.BAD_REQUEST, "medical-record.record-id-incorrect", "Medical Record ID không chính xác, vui lòng kiểm tra lại"),

    // ROLE EXCEPTION
    ROLE_NOT_EXISTED("ROLE_NOT_EXISTED", HttpStatus.NOT_FOUND, "role.not-existed", "Vai trò không tồn tại trong hệ thống"),

    // ACCOUNT EXCEPTION
    USERNAME_EXISTED("USERNAME_EXISTED", HttpStatus.BAD_REQUEST, "account.username-existed", "Tên tài khoản đã tồn tại, vui lòng kiểm tra lại"),
    ACCOUNT_DATA_INVALID("ACCOUNT_DATA_INVALID", HttpStatus.BAD_REQUEST, "account.data-invalid", "Thông tin tài khoản không hợp lệ, vui lòng kiểm tra lại"),
    ACCOUNT_NOT_EXISTED("ACCOUNT_NOT_EXISTED", HttpStatus.NOT_FOUND, "account.not-existed", "Thông tin tài khoản không tồn tại"),
    USERNAME_INVALID("USERNAME_INVALID", HttpStatus.BAD_REQUEST, "account.change-pass-fail", "Không thể đổi password của tài khoản khác, vui lòng kiểm tra lại"),
    PASSWORD_NOT_MATCH("PASSWORD_NOT_MATCH", HttpStatus.BAD_REQUEST, "account.password-not-match", "Mật khẩu không khớp, vui lòng kiểm tra lại"),
    PASSWORD_INCORRECT("PASSWORD_INCORRECT", HttpStatus.BAD_REQUEST, "account.password-incorrect", "Mật khẩu không đúng, vui lòng kiểm tra lại"),
    ACCOUNT_NOT_LOCK("ACCOUNT_NOT_LOCK", HttpStatus.BAD_REQUEST, "account.not-lock", "Tài khoản đang mở không thể thực hiện mở khoá, vui lòng kiểm tra lại"),
    PASSWORD_INVALID("PASSWORD_INVALID", HttpStatus.BAD_REQUEST, "account.not-equals", "Mật khẩu mới không được giống mật khẩu hiện tại, vui lòng kiểm tra lại"),
    NOT_PERMISSION("MEDICAL_STAFF_NOT_PERMISSION", HttpStatus.BAD_REQUEST, "account.medical-staff-not-permission", "Quyền truy cập không phù hợp với chuyên môn"),

    // LOGIN EXCEPTION
    AUTHENTICATION_DATA_INVALID("AUTHENTICATION_DATA_INVALID", HttpStatus.BAD_REQUEST, "authentication.data-invalid", "Thông tin đăng nhập không hợp lệ, vui lòng kiểm tra lại"),
    USERNAME_OR_PASSWORD_INCORRECT("USERNAME_OR_PASSWORD_INCORRECT", HttpStatus.BAD_REQUEST, "authentication.login-fail", "Tài khoản hoặc mật khẩu không đúng, vui lòng kiểm tra lại"),
    LOGIN_FAIL("LOGIN_FAIL", HttpStatus.BAD_REQUEST, "token.not-verify", "Token chưa được xác thực, vui lòng kiểm tra lại"),
    TOKEN_INVALID("TOKEN_INVALID", HttpStatus.BAD_REQUEST, "token.invalid", "Token không hợp lệ, vui lòng kiểm tra lại"),
    ACCOUNT_LOCK_NOT_LOGIN("ACCOUNT_LOCK_NOT_LOGIN", HttpStatus.LOCKED, "account.locked", "Tài khoản của bạn đã bị khóa, vui lòng liên hệ quản trị viên"),

    // RESULT EXCEPTION
    CREATE_DIRECTORY_FAIL("CREATE_DIRECTORY_FAIL", HttpStatus.BAD_REQUEST, "directory.not-create", "Thư mục lưu trữ kết quả chưa được khởi tạo"),
    FILE_NAME_ERROR("FILE_NAME_ERROR", HttpStatus.BAD_REQUEST, "file.name-error", "Tên file tải lên không hợp lệ"),
    FILE_NOT_FOUND("FILE_NOT_FOUND", HttpStatus.NOT_FOUND, "file.not-found", "File đính kèm không tồn tại"),
    PATH_NOT_EXISTED("PATH_NOT_EXISTED", HttpStatus.NOT_FOUND, "file.path-not-found", "Đường dẫn của file không tồn tại"),
    FILE_FORMAT_NOT_EQUAL("FILE_FORMAT_NOT_EQUAL", HttpStatus.BAD_REQUEST, "file.content-type-not-equal", "Định dạng file không trùng khớp"),
    FILE_FORMAT_NOT_ALLOW("FILE_FORMAT_NOT_ALLOW", HttpStatus.BAD_REQUEST, "file.wrong-format", "Định dạng file không đuợc cho phép"),

    //RESULT EXCEPTION
    RESULT_NOT_FOUND("RESULT_NOT_FOUND", HttpStatus.NOT_FOUND, "result.not-found", "File kết quả không tồn tại, vui lòng kiểm tra lại"),
    DEPARTMENT_HAD_ROOM("DEPARTMENT_HAD_ROOM", HttpStatus.BAD_REQUEST, "department.delete-fail", "Khoa bệnh đã có buồng bệnh không thể xoá"),
    DEPARTMENT_HAD_PEOPLE("DEPARTMENT_HAD_PEOPLE", HttpStatus.BAD_REQUEST, "department.delete-fail", "Khoa bệnh đã có nhân viên và bệnh nhân không thể xoá"),
    DELETE_PATIENT_BED_FAIL("DELETE_PATIENT_BED_FAIL", HttpStatus.BAD_REQUEST, "patient-bed.delete-fail", "Giường bệnh đang có bệnh nhân, không thể xoá"),
    UNAUTHORIZED("UNAUTHORIZED", HttpStatus.UNAUTHORIZED, "login.claims-null", "Không có quyền truy cập"),

    // CONVERT GENDER EXCEPTION
    CONVERT_GENDER_ENUM_ERROR("CONVERT_GENDER_ENUM_ERROR", HttpStatus.BAD_REQUEST, "gender.convert-error", "convert gender error"),

    ;
    private String errorCode;
    private HttpStatus httpStatus;
    private String messageKey;
    private String messageDefault;

    EnumPMSException(String errorCode, HttpStatus httpStatus, String messageKey, String messageDefault) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
        this.messageDefault = messageDefault;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String getMessageDefault() {
        return messageDefault;
    }

    @Override
    public String toString() {
        return "EnumCommonException{" +
                "errorCode='" + errorCode + '\'' +
                ", httpStatus=" + httpStatus +
                ", messageKey='" + messageKey + '\'' +
                ", messageDefault='" + messageDefault + '\'' +
                '}';
    }
}
