package com.binhnd.pmsbe.controller;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.dto.request.MedicalRecordDetailRequest;
import com.binhnd.pmsbe.dto.request.MedicalRecordRequest;
import com.binhnd.pmsbe.dto.response.MedicalRecordDetailResponse;
import com.binhnd.pmsbe.dto.response.MedicalRecordResponse;
import com.binhnd.pmsbe.services.medical_record.MedicalRecordCUService;
import com.binhnd.pmsbe.services.medical_record.MedicalRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.binhnd.pmsbe.common.constants.RequestAction.MEDICAL_RECORD;
import static com.binhnd.pmsbe.common.constants.RequestAction.MedicalRecord.*;

@RestController
@RequestMapping(PMSConstants.PREFIX_URL + MEDICAL_RECORD)
@Validated
public class MedicalRecordController {

    private final Logger L = LoggerFactory.getLogger(this.getClass());

    private final MedicalRecordService medicalRecordService;
    private final MedicalRecordCUService medicalRecordCUService;

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService,
                                   MedicalRecordCUService medicalRecordCUService) {
        this.medicalRecordService = medicalRecordService;
        this.medicalRecordCUService = medicalRecordCUService;
    }

    @PutMapping(value = UPDATE_RECORD, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicalRecordResponse> updateRecord(@RequestParam Long id, @RequestBody MedicalRecordRequest request) {
        L.info("[PUT] {}: update info medical record", PMSConstants.PREFIX_URL + "/medical-record/update?id=" + id);
        return ResponseEntity.ok().body(medicalRecordCUService.updateRecord(id, request));
    }

    @PostMapping(value = CREATE_RECORD_DETAIL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createRecordDetails(@RequestBody MedicalRecordDetailRequest request) {
        L.info("[POST] {}: create info medical record detail", PMSConstants.PREFIX_URL + "/medical-record/create-detail");
        medicalRecordCUService.createRecordDetail(request);
        return ResponseEntity.ok().body("\"Create medical record detail successfully!\"");
    }

    @PutMapping(value = UPDATE_RECORD_DETAIL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateRecordDetails(@RequestParam Long id, @RequestBody MedicalRecordDetailRequest request) {
        L.info("[PUT] {}: update a medical record details", PMSConstants.PREFIX_URL + "/medical-record/update-detail?id=" + id);
        medicalRecordCUService.updateRecordDetail(id, request);
        return ResponseEntity.ok().body("\"Update medical record detail successfully!\"");
    }

    @DeleteMapping(value = DELETE_RECORD_DETAIL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteRecordDetail(@RequestParam Long id) {
        L.info("[DELETE] {}: delete a medical record details", PMSConstants.PREFIX_URL + "/medical-record/delete-detail?id=" + id);
        medicalRecordCUService.deleteRecordDetail(id);
        return ResponseEntity.ok().body("\"Record details deleted successfully!\"");
    }

    @GetMapping(value = FIND_BY_PATIENT_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicalRecordResponse> findMedicalRecordByPatientId(@RequestParam Long id) {
        L.info("[GET] {}: find medical record by patient id", PMSConstants.PREFIX_URL + "/medical-record/find-by-patient?id=" + id);
        return ResponseEntity.ok().body(medicalRecordService.findMedicalRecordByPatientId(id));
    }

    @GetMapping(value = FIND_ALL_DETAIL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicalRecordDetailResponse>> findAllMedicalRecordDetail(@RequestParam Long id) {
        L.info("[GET] {}: find all medical record detail", PMSConstants.PREFIX_URL + "/medical-record/find-all-detail?id=" + id);
        return ResponseEntity.ok().body(medicalRecordService.findAllMedicalRecordDetail(id));
    }

    @GetMapping(value = FIND_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicalRecordResponse> findMedicalRecordById(@RequestParam Long id) {
        L.info("[GET] {}: find medical record by patient id", PMSConstants.PREFIX_URL + "/medical-record/find-by-id?id=" + id);
        return ResponseEntity.ok().body(medicalRecordService.findById(id));
    }

    @GetMapping(value = FIND_DETAIL_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicalRecordDetailResponse> findDetailById(@RequestParam Long id) {
        L.info("[GET] {}: find medical record by patient id", PMSConstants.PREFIX_URL + "/medical-record/find-detail-by-id?id=" + id);
        return ResponseEntity.ok().body(medicalRecordService.findMedicalRecordDetailById(id));
    }

}
