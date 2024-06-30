package com.binhnd.pmsbe.controller;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.common.utils.ExcelUtils;
import com.binhnd.pmsbe.dto.request.MedicalStaffRequest;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.MedicalStaffResponse;
import com.binhnd.pmsbe.services.excel.ExportExcelService;
import com.binhnd.pmsbe.services.medical_staff.MedicalStaffCUService;
import com.binhnd.pmsbe.services.medical_staff.MedicalStaffService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.binhnd.pmsbe.common.constants.RequestAction.Department.DOWNLOAD_EXCEL;
import static com.binhnd.pmsbe.common.constants.RequestAction.MEDICAL_STAFF;
import static com.binhnd.pmsbe.common.constants.RequestAction.MedicalStaff.*;
import static com.binhnd.pmsbe.common.utils.ExcelUtils.sendExcelToClient;

@RestController
@RequestMapping(PMSConstants.PREFIX_URL + MEDICAL_STAFF)
public class MedicalStaffController {

    private final Logger L = LoggerFactory.getLogger(MedicalStaffController.class);

    private final MedicalStaffService medicalStaffService;
    private final MedicalStaffCUService medicalStaffCUService;

    private final ExportExcelService medicalStaffExcelExportService;

    @Autowired
    public MedicalStaffController(MedicalStaffService medicalStaffService,
                                  MedicalStaffCUService medicalStaffCUService, ExportExcelService medicalStaffExcelExportService) {
        this.medicalStaffService = medicalStaffService;
        this.medicalStaffCUService = medicalStaffCUService;
        this.medicalStaffExcelExportService = medicalStaffExcelExportService;
    }

    @GetMapping(value = FIND_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicalStaffResponse>> findAllMedicalStaff() {
        L.info("[GET]{}/ get all medical-staff", PMSConstants.PREFIX_URL + "/medical-staff/find-all");
        return ResponseEntity.ok().body(medicalStaffService.findAllMedicalStaff());
    }

    @GetMapping(value = FIND_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicalStaffResponse> findMedicalStaffById(@RequestParam Long id) {
        L.info("[GET]{}/ get medical staff by id", String.format("%smedical-staff/find-by-id?id=%d", PMSConstants.PREFIX_URL, id));
        return ResponseEntity.ok().body(medicalStaffService.findMedicalStaffById(id));
    }

    @GetMapping(value = FIND_BY_DEPARTMENT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicalStaffResponse>> findAllMedicalStaffByDepartmentId(@RequestParam Long id) {
        L.info("[GET] {}: get all medical staff by departmentId", PMSConstants.PREFIX_URL + "/medical-staff/find-by-department?id=" + id);
        return ResponseEntity.ok().body(medicalStaffService.findAllMedicalStaffByDepartmentId(id));
    }

    @GetMapping(value = DOWNLOAD_EXCEL, produces = ExcelUtils.EXCEL_TYPE)
    public ResponseEntity<InputStreamResource> downloadDepartmentExcel(HttpServletResponse response) {
        L.info("[GET]/medical-staff/download-excel :: Download department list Excel file");
        Workbook workbook = medicalStaffExcelExportService.downloadExcel();
        return sendExcelToClient(workbook, response, "medical-staff");
    }

    @GetMapping(value = FIND_ALL_DOCTOR, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicalStaffResponse>> findAllDoctorByDepartment(@RequestParam Long id) {
        L.info("[GET] {}: get all medical staff by departmentId", PMSConstants.PREFIX_URL + "/medical-staff/find-all-doctor?id=" + id);
        return ResponseEntity.ok().body(medicalStaffService.findAllDoctorByDepartment(id));
    }

    @GetMapping(value = FIND_ALL_PAGE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<MedicalStaffResponse>> findAllMedicalStaffPage(SearchSortPageableDTO dto) {
        L.info("[GET] {}: get all medical staff page", PMSConstants.PREFIX_URL + "/medical-staff/find-all/page");
        return ResponseEntity.ok().body(medicalStaffService.findAllMedicalStaffPage(dto));
    }

    @GetMapping(value = FIND_BY_DEPARTMENT_PAGE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<MedicalStaffResponse>> findAllMedicalStaffByDepartmentIdPage(SearchSortPageableDTO dto, @RequestParam Long id) {
        L.info("[GET] {}: get all medical staff by department id page", PMSConstants.PREFIX_URL + "/medical-staff/find-all-by-departmentId/page?id=" + id);
        return ResponseEntity.ok().body(medicalStaffService.findAllMedicalStaffByDepartmentIdPage(id, dto));
    }

    @GetMapping(value = FIND_ALL_NO_ACCOUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicalStaffResponse>> findMedicalStaffByAccountIsNull() {
        L.info("[GET] {}: get all medical staff no account", PMSConstants.PREFIX_URL + "/medical-staff/find-all-no-account");
        return ResponseEntity.ok().body(medicalStaffService.findMedicalStaffByAccountIsNull());
    }

    @PostMapping(value = CREATE_MEDICAL_STAFF, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicalStaffResponse> createMedicalStaff(@RequestBody MedicalStaffRequest request) {
        L.info("[POST]{}/ create a new medical staff", PMSConstants.PREFIX_URL + "/medical-staff/create");
        MedicalStaffResponse response = medicalStaffCUService.createMedicalStaff(request);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(value = UPDATE_MEDICAL_STAFF, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicalStaffResponse> updateMedicalStaff(@RequestBody MedicalStaffRequest request, @RequestParam Long id) {
        L.info("[PUT] {}: update a medical staff", PMSConstants.PREFIX_URL + "/medical-staff/update");
        MedicalStaffResponse response = medicalStaffCUService.updateMedicalStaff(request, id);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(value = DELETE_MEDICAL_STAFF, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteMedicalStaff(@RequestParam Long id) {
        L.info("[DELETE] {}: delete a medical staff", String.format("%s/medical-staff/delete?id=%d", PMSConstants.PREFIX_URL, id));
        medicalStaffCUService.deleteMedicalStaff(id);
        return ResponseEntity.ok().body("\"Medical staff deleted successfully!\"");
    }
}
