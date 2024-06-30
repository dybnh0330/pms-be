package com.binhnd.pmsbe.controller;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.dto.request.PatientBedRequest;
import com.binhnd.pmsbe.dto.request.SearchSortDto;
import com.binhnd.pmsbe.dto.response.PatientBedResponse;
import com.binhnd.pmsbe.services.patient_bed.PatientBedCUService;
import com.binhnd.pmsbe.services.patient_bed.PatientBedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.binhnd.pmsbe.common.constants.RequestAction.PATIENT_BED;
import static com.binhnd.pmsbe.common.constants.RequestAction.PatientBed.*;

@RestController
@RequestMapping(PMSConstants.PREFIX_URL + PATIENT_BED)
public class PatientBedController {

    private final Logger L = LoggerFactory.getLogger(PatientBedController.class);

    private final PatientBedCUService patientBedCUService;
    private final PatientBedService patientBedService;

    @Autowired
    public PatientBedController(PatientBedCUService patientBedCUService, PatientBedService patientBedService) {
        this.patientBedCUService = patientBedCUService;
        this.patientBedService = patientBedService;
    }

    @PostMapping(value = CREATE_BED, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientBedResponse> createPatientBed (@RequestBody PatientBedRequest request) {
        L.info("[POST] {}: create new patient bed", PMSConstants.PREFIX_URL + "/patient-bed/create");
        return ResponseEntity.ok().body(patientBedCUService.createPatientBed(request));
    }

    @PutMapping(value = UPDATE_BED, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientBedResponse> updatePatientBed (@RequestBody PatientBedRequest request, @RequestParam Long id) {
        L.info("[PUT] {}: update patient bed", PMSConstants.PREFIX_URL + "/patient-bed/update?id=" + id);
        return ResponseEntity.ok().body(patientBedCUService.updatePatientBed(request, id));
    }

    @DeleteMapping(value = DELETE_BED, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deletePatientBed (@RequestParam Long id) {
        L.info("[DELETE] {}: delete a patient bed in patient room", PMSConstants.PREFIX_URL + "/patient-bed/delete?id=" + id);
        patientBedCUService.deletePatientBed(id);
        return ResponseEntity.ok().body("\"Patient bed deleted successfully!\"");
    }

    @DeleteMapping(value = DELETE_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteAllPatientBedInRoom (@RequestParam Long id) {
        L.info("[DELETE] {}: delete all patient bed in patient room", PMSConstants.PREFIX_URL + "/patient-bed/delete-all?id=" + id);
        patientBedCUService.deleteAllPatientBed(id);
        return ResponseEntity.ok().body("\"All patient bed in room id = " + id + " deleted successfully!\"");
    }

    @GetMapping(value = FIND_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientBedResponse> findPatientBedById (@RequestParam Long id) {
        L.info("[GET] {}: find patient bed by id", PMSConstants.PREFIX_URL + "/patient-bed/find-by-id?id=" + id);
        return ResponseEntity.ok().body(patientBedService.findPatientBedById(id));
    }

    @GetMapping(value = FIND_BY_ROOM, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PatientBedResponse>> findAllPatientBedByRoomId (@RequestParam Long id) {
        L.info("[GET] {}: find all patient bed by room id", PMSConstants.PREFIX_URL + "/patient-bed-by-room?id=" + id);
        return ResponseEntity.ok().body(patientBedService.findAllBedByRoomId(id));
    }

    @GetMapping(value = FIND_EMPTY_BY_ROOM, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PatientBedResponse>> findAllBedEmptyByRoom (@RequestParam Long id) {
        L.info("[GET] {}: find all patient bed by room id", PMSConstants.PREFIX_URL + "/find-bed-empty-by-room?id=" + id);
        return ResponseEntity.ok().body(patientBedService.findAllBedEmptyByRoom(id));
    }
}
