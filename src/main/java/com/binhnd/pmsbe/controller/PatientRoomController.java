package com.binhnd.pmsbe.controller;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.dto.request.PatientRoomHasBedRequest;
import com.binhnd.pmsbe.dto.request.PatientRoomRequest;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.PatientRoomResponse;
import com.binhnd.pmsbe.services.patient_room.PatientRoomCUService;
import com.binhnd.pmsbe.services.patient_room.PatientRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.binhnd.pmsbe.common.constants.RequestAction.PATIENT_ROOM;
import static com.binhnd.pmsbe.common.constants.RequestAction.PatientRoom.*;

@RestController
@RequestMapping(value = PMSConstants.PREFIX_URL + PATIENT_ROOM)
public class PatientRoomController {

    private final Logger L = LoggerFactory.getLogger(PatientRoomController.class);

    private final PatientRoomService patientRoomService;
    private final PatientRoomCUService patientRoomCUService;

    public PatientRoomController(PatientRoomService patientRoomService,
                                 PatientRoomCUService patientRoomCUService) {
        this.patientRoomService = patientRoomService;
        this.patientRoomCUService = patientRoomCUService;
    }

    @PostMapping(value = CREATE_ROOM, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientRoomResponse> createPatientRoom(@RequestBody PatientRoomHasBedRequest request) {
        L.info("[POST] {}: create new patient room", PMSConstants.PREFIX_URL + "patient-room/create");
        return ResponseEntity.ok().body(patientRoomCUService.createPatientRoom(request));
    }

    @PutMapping(value = UPDATE_ROOM, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientRoomResponse> updatePatientRoom(@RequestBody PatientRoomRequest request, @RequestParam Long id) {
        L.info("[PUT] {}: update a patient room existed", PMSConstants.PREFIX_URL + "/patient-room/update?id=" + id);
        return ResponseEntity.ok().body(patientRoomCUService.updatePatientRoom(request, id));
    }

    @DeleteMapping(value = DELETE_ROOM, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deletePatientRoom(@RequestParam Long id) {
        L.info("[DELETE] {}: delete a patient room", PMSConstants.PREFIX_URL + "/patient-room/delete?id=" + id);
        patientRoomCUService.deletePatientRoom(id);
        return ResponseEntity.ok().body("\"Patient room deleted successfully!\"");
    }

    @GetMapping(value = FIND_BY_DEPARTMENT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PatientRoomResponse>> findAllPatientRoomByDepartmentId(@RequestParam Long id) {
        L.info("[GET] {}: find all patient room by department id", PMSConstants.PREFIX_URL + "/patient-room-by-department?id=" + id);
        return ResponseEntity.ok().body(patientRoomService.findAllPatientRoomByDepartmentId(id));
    }

    @GetMapping(value = FIND_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientRoomResponse> findPatientRoomById(@RequestParam Long id) {
        L.info("[GET] {}: find patient room by id", PMSConstants.PREFIX_URL + "/patient-room/find-by-id?id=" + id);
        return ResponseEntity.ok().body(patientRoomService.findPatientRoomById(id));
    }


    @GetMapping(value = FIND_ROOM_EMPTY_BY_DEPARTMENT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PatientRoomResponse>> findAllPatientRoomEmptyByDepartment(@RequestParam Long id) {
        L.info("[GET] {}: find all patient room empty by department", PMSConstants.PREFIX_URL + "/patient-room/find-room-empty-by-department?id=" + id);
        return ResponseEntity.ok().body(patientRoomService.findAllPatientRoomEmptyByDepartmentId(id));
    }

    @GetMapping(value = FIND_BY_DEPARTMENT_PAGE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PatientRoomResponse>> findAllPatientRoomByDepartmentPage (@RequestParam Long id, SearchSortPageableDTO dto) {
        L.info("[GET] {}: find all patient room by department page", PMSConstants.PREFIX_URL + "/page/patient-room/find-by-department?id=" + id);
        return ResponseEntity.ok().body(patientRoomService.findAllPatientRoomByDepartmentPage(id, dto));
    }
}
