package com.binhnd.pmsbe.controller;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.dto.request.PatientInHospitalRequest;
import com.binhnd.pmsbe.dto.request.PatientInfoRequest;
import com.binhnd.pmsbe.dto.request.SearchSortDto;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.PatientAdmissionResponse;
import com.binhnd.pmsbe.dto.response.PatientResponse;
import com.binhnd.pmsbe.services.patient.PatientCUService;
import com.binhnd.pmsbe.services.patient.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.binhnd.pmsbe.common.constants.RequestAction.PATIENT;
import static com.binhnd.pmsbe.common.constants.RequestAction.Patient.*;

@RestController
@RequestMapping(PMSConstants.PREFIX_URL + PATIENT)
public class PatientController {

    private final Logger L = LoggerFactory.getLogger(PatientController.class);
    private final PatientCUService patientCUService;
    private final PatientService patientService;

    public PatientController(PatientCUService patientCUService,
                             PatientService patientService) {
        this.patientCUService = patientCUService;
        this.patientService = patientService;
    }

    @PutMapping(value = UPDATE_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientResponse> updateInfoPatient(@RequestBody PatientInfoRequest request, @RequestParam Long id) {
        L.info("[PUT] {}: update info patient in hospital", PMSConstants.PREFIX_URL + "/patient/update-info?id=" + id);
        return ResponseEntity.ok().body(patientCUService.updateInfoPatient(request, id));
    }

    @PutMapping(value = UPDATE_IN_DEPARTMENT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientResponse> updatePatientInDepartment(@RequestBody PatientInHospitalRequest request, @RequestParam Long id) {
        L.info("[PUT] {}: update patient in department", PMSConstants.PREFIX_URL + "/patient/update-in-department?id=" + id);
        return ResponseEntity.ok().body(patientCUService.updatePatientInDepartment(request, id));
    }

    @DeleteMapping(value = DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deletePatient(@RequestParam Long id) {
        L.info("[DELETE] {}: delete a patient in department", PMSConstants.PREFIX_URL + "/patient/delete?id=" + id);
        patientCUService.deletePatient(id);
        return ResponseEntity.ok().body("\"Patient deleted successfully!\"");
    }

    @GetMapping(value = FIND_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PatientResponse>> findAllPatient() {
        L.info("[GET] {}: find all patient in hospital", PMSConstants.PREFIX_URL + "/patient/find-all");
        return ResponseEntity.ok().body(patientService.findAllPatient());
    }

    @GetMapping(value = FIND_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientResponse> findPatientById(@RequestParam Long id) {
        L.info("[GET] {}: find patient by id", PMSConstants.PREFIX_URL + "/patient/find-by-id?id=" + id);
        return ResponseEntity.ok().body(patientService.findPatientById(id));
    }

    @GetMapping(value = FIND_BY_DEPARTMENT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PatientResponse>> findAllPatientInDepartment(@RequestParam Long id) {
        L.info("[GET] {}: find all patient in department", PMSConstants.PREFIX_URL + "/patient/find-by-department?id=" + id);
        return ResponseEntity.ok().body(patientService.findAllPatientByDepartment(id));
    }

    @GetMapping(value = FIND_BY_DEPARTMENT_PAGE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PatientResponse>> findAllPatientInDepartmentPage(@RequestParam Long id, SearchSortPageableDTO dto) {
        L.info("[GET] {}: find all patient in department page", PMSConstants.PREFIX_URL + "/patient/page/find-by-department?id=" + id);
        return ResponseEntity.ok().body(patientService.findAllPatientByDeparmentPage(id, dto));
    }

    @GetMapping(value = FIND_PATIENTS_ADMISSION, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PatientAdmissionResponse>> findAllPatientAdmission(SearchSortDto dto) {
        L.info("[GET] {}: find all patient in department", PMSConstants.PREFIX_URL + "/patient/find-patients-admission");
        return ResponseEntity.ok().body(patientService.findPatientByDepartmentIsNull(dto));
    }

    @GetMapping(value = FIND_PATIENTS_ADMISSION_DEPARTMENT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PatientResponse>> findAllPatientsAdmissionDepartment(@RequestParam Long id) {
        L.info("[GET] {}: find all patient admission in department", PMSConstants.PREFIX_URL + "/patient/find-patients-admission-department");
        return ResponseEntity.ok().body(patientService.findPatientByMedicalStaffIsNull(id));
    }
}
