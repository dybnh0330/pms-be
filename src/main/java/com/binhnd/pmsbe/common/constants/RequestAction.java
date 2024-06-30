package com.binhnd.pmsbe.common.constants;

public final class RequestAction {

    private RequestAction() {}

    public static final String DASHBOARD = "/dashboard";
    public static final String ACCOUNT = "/account";
    public static final String ROLE = "/role";
    public static final String CATEGORY = "/category";
    public static final String DEPARTMENT = "/department";
    public static final String MEDICAL_ORDER = "/medical-order";
    public static final String MEDICAL_RECORD = "/medical-record";
    public static final String MEDICAL_STAFF = "/medical-staff";
    public static final String PATIENT_BED = "/patient-bed";
    public static final String PATIENT = "/patient";
    public static final String PATIENT_ROOM = "/patient-room";
    public static final String RECEIVE_PATIENT = "/receive-patient";
    public static final String STAFF_ROOM = "/staff-room";
    public static final String RESULT = "/result";

    public static class Account {

        private Account() {}

        public static final String CREATE_ACCOUNT = "/create";
        public static final String UPDATE_ACCOUNT = "/update";
        public static final String DELETE_ACCOUNT = "/delete";
        public static final String UNLOCK = "/unlock";
        public static final String CHANGE_PASSWORD = "/change-password";
        public static final String RESET_PASSWORD = "/reset-password";
        public static final String FIND_BY_ID = "/find-by-id";
        public static final String FIND_ALL = "/find-all";
        public static final String FIND_ALL_PAGE = "/page/find-all";
    }
    public static class Authentication {

        private Authentication() {}

        public static final String LOGIN = "/login";
        public static final String REFRESH_TOKEN = "/refresh-token";

    }

    public static class Role {
        private Role() {}

        public static final String FIND_ALL = "/find-all";
    }

    public static class Category {


        private Category() {}

        public static final String FIND_ALL_PAGE = "/page/find-all";
        public static final String CREATE_CATEGORY = "/create";
        public static final String UPDATE_CATEGORY = "/update";
        public static final String DELETE_CATEGORY = "/delete";
        public static final String FIND_ALL = "/find-all";
        public static final String FIND_BY_ID = "/find-by-id";
        public static final String FIND_BY_TYPE = "/find-by-type";
        public static final String FIND_ALL_TYPE = "/type/find-all";
        public static final String FIND_PAGE_BY_TYPE = "/page/find-by-type";
    }

    public static class Department {

        private Department() {}

        public static final String FIND_ALL = "/find-all";
        public static final String FIND_ALL_PAGE = "/page/find-all";
        public static final String FIND_BY_ID = "/find-by-id";
        public static final String CREATE_DEPARTMENT = "/create";
        public static final String UPDATE_DEPARTMENT = "/update";
        public static final String DELETE_DEPARTMENT = "/delete";
        public static final String DOWNLOAD_EXCEL = "/download-excel";
    }

    public static class MedicalOrder {

        private MedicalOrder() {}


        public static final String FIND_BY_PATIENT = "/find-by-patient";
        public static final String ADD_DETAIL = "/add-detail";
        public static final String CANCEL_ORDER = "/cancel-order";
        public static final String FIND_ALL = "/find-all";
        public static final String FIND_BY_ID = "/find-by-id";
    }

    public static class MedicalRecord {



        private MedicalRecord() {}
        public static final String FIND_DETAIL_BY_ID = "/find-detail-by-id";

        public static final String FIND_BY_ID = "/find-by-id";
        public static final String UPDATE_RECORD = "/update";
        public static final String CREATE_RECORD_DETAIL = "/create-detail";
        public static final String UPDATE_RECORD_DETAIL = "/update-detail";
        public static final String DELETE_RECORD_DETAIL = "/delete-detail";
        public static final String FIND_BY_PATIENT_ID = "/find-by-patient";
        public static final String FIND_ALL_DETAIL = "/find-all-detail";

    }

    public static class MedicalStaff {



        private MedicalStaff() {}

        public static final Object DOWNLOAD_EXCEL = "/download-excel";
        public static final String FIND_ALL = "/find-all";
        public static final String FIND_ALL_DOCTOR = "/find-all-doctor";
        public static final String FIND_BY_ID = "/find-by-id";
        public static final String FIND_BY_DEPARTMENT = "/find-by-department";
        public static final String FIND_ALL_PAGE = "/page/find-all";
        public static final String FIND_BY_DEPARTMENT_PAGE = "/page/find-by-department";
        public static final String CREATE_MEDICAL_STAFF = "/create";
        public static final String UPDATE_MEDICAL_STAFF = "/update";
        public static final String DELETE_MEDICAL_STAFF = "/delete";
        public static final String FIND_ALL_NO_ACCOUNT = "/find-all-no-account";

    }

    public static class PatientBed {


        private PatientBed() {}

        public static final String FIND_EMPTY_BY_ROOM = "/find-bed-empty-by-room";
        public static final String CREATE_BED = "/create";
        public static final String UPDATE_BED = "/update";
        public static final String DELETE_BED = "/delete";
        public static final String DELETE_ALL = "/delete-all";
        public static final String FIND_BY_ID = "/find-by-id";
        public static final String FIND_BY_ROOM = "/find-by-room";

    }

    public static class Patient {

        private Patient() {}

        public static final String FIND_PATIENTS_ADMISSION_DEPARTMENT = "/find-patients-admission-department";
        public static final String FIND_PATIENTS_ADMISSION = "/find-patients-admission";
        public static final String UPDATE_INFO = "/update-info";
        public static final String UPDATE_IN_DEPARTMENT = "/update-in-department";
        public static final String DELETE = "/delete";
        public static final String FIND_ALL = "/find-all";
        public static final String FIND_BY_ID = "/find-by-id";
        public static final String FIND_BY_DEPARTMENT = "/find-by-department";
        public static final String FIND_BY_DEPARTMENT_PAGE = "/page/find-by-department";

    }

    public static class PatientRoom {

        private PatientRoom() {}

        public static final String CREATE_ROOM = "/create";
        public static final String UPDATE_ROOM = "/update";
        public static final String DELETE_ROOM = "/delete";
        public static final String FIND_BY_DEPARTMENT = "/find-by-department";
        public static final String FIND_BY_ID = "/find-by-id";
        public static final String FIND_ROOM_EMPTY_BY_DEPARTMENT = "/find-room-empty-by-department";
        public static final String FIND_BY_DEPARTMENT_PAGE = "/page/find-by-department";

    }

    public static class StaffRoom {

        private StaffRoom() {}

        public static final String UPDATE_STAFF_ROOM = "/update";
        public static final String FIND_STAFF_IN_ROOM = "/find-staff-in-room";

    }
    public static class Result {

        private Result() {}

        public static final String ADD_RESULT = "/add";
        public static final String DELETE_RESULT = "/delete";
        public static final String FIND_ALL = "/find-all";
        public static final String FIND_BY_ID = "/find-by-id";
        public static final String DOWNLOAD_RESULT = "/downloadFile";

    }

    public static class ReceivePatient {

        private ReceivePatient() {}

        public static final String CREATE_PATIENT = "/create";
        public static final String ORDER_DEPARTMENT = "/order-department";
        public static final String ORDER_MEDICAL_ORDER = "/order-medical-order";

    }
}

