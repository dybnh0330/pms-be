package com.binhnd.pmsbe.controller;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.dto.request.OrderDepartment;
import com.binhnd.pmsbe.dto.request.OrderMedicalOrder;
import com.binhnd.pmsbe.dto.request.ReceivePatientRequest;
import com.binhnd.pmsbe.services.receive_patient.ReceivePatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.binhnd.pmsbe.common.constants.RequestAction.RECEIVE_PATIENT;
import static com.binhnd.pmsbe.common.constants.RequestAction.ReceivePatient.*;

@RestController
@RequestMapping(value = PMSConstants.PREFIX_URL + RECEIVE_PATIENT)
public class ReceivePatientController {

    private final Logger L = LoggerFactory.getLogger(ReceivePatientController.class);
    private final ReceivePatientService receivePatientService;

    @Autowired
    public ReceivePatientController(ReceivePatientService receivePatientService) {
        this.receivePatientService = receivePatientService;
    }

    @PostMapping(value = CREATE_PATIENT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> receivePatient(@RequestBody ReceivePatientRequest request) {
        L.info("[POST] {}: receive patient in hospital", PMSConstants.PREFIX_URL + "/receive-patient");
        receivePatientService.receivePatient(request);
        return ResponseEntity.ok().body("\"Receive patient successfully!\"");
    }

    @PutMapping(value = ORDER_DEPARTMENT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> orderPatientInDepartment(@RequestParam Long id, @RequestBody OrderDepartment request) {
        L.info("[PUT] {}: update patient in department", PMSConstants.PREFIX_URL + "/receive-patient/order-department?id=" + id);
        receivePatientService.orderDepartment(id, request);
        return ResponseEntity.ok().body("\"Update patient in department successfully!\"");
    }

    @PutMapping(value = ORDER_MEDICAL_ORDER, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> orderMedicalOrder(@RequestParam Long id, @RequestBody OrderMedicalOrder request) {
        L.info("[PUT] {}: update medical order for patient", PMSConstants.PREFIX_URL + "/receive-patient/order-medical-order?id=" + id);
        receivePatientService.orderMedicalOrder(id, request);
        return ResponseEntity.ok().body("\"Update medical order for patient successfully!\"");
    }
}
