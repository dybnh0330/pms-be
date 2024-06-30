package com.binhnd.pmsbe.controller;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.dto.request.MedicalOrderRequest;
import com.binhnd.pmsbe.dto.response.MedicalOrderDetailsResponse;
import com.binhnd.pmsbe.dto.response.MedicalOrderResponse;
import com.binhnd.pmsbe.services.medical_order.MedicalOrderCUService;
import com.binhnd.pmsbe.services.medical_order.MedicalOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.binhnd.pmsbe.common.constants.RequestAction.MEDICAL_ORDER;
import static com.binhnd.pmsbe.common.constants.RequestAction.MedicalOrder.*;

@RestController
@RequestMapping(PMSConstants.PREFIX_URL + MEDICAL_ORDER)
public class MedicalOrderController {

    private final Logger L = LoggerFactory.getLogger(MedicalOrderController.class);
    private final MedicalOrderCUService medicalOrderCUService;
    private final MedicalOrderService medicalOrderService;

    public MedicalOrderController(MedicalOrderCUService medicalOrderCUService,
                                   MedicalOrderService medicalOrderService) {
        this.medicalOrderCUService = medicalOrderCUService;
        this.medicalOrderService = medicalOrderService;
    }

    @PostMapping(value = ADD_DETAIL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addMedicalOrderDetail(@RequestBody List<MedicalOrderRequest> request) {
        L.info("[POST] {}: add medical order detail", PMSConstants.PREFIX_URL + "/medical-order/add-detail");
        medicalOrderCUService.addMedicalOrderDetail(request);
        return ResponseEntity.ok().body("\"Add medical order successfully!\"");
    }

    @DeleteMapping(value = CANCEL_ORDER, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> cancelMedicalOrderDetails (@RequestParam Long id) {
        L.info("[DELETE] {}: cancel medical order detail", PMSConstants.PREFIX_URL + "/medical-order/cancel-order?id=" + id);
        medicalOrderCUService.cancelMedicalOrderDetails(id);
        return ResponseEntity.ok().body("\"Cancel medical order successfully!\"");
    }

    @GetMapping(value = FIND_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicalOrderDetailsResponse>> findMedicalOrderDetails(@RequestParam Long id, @RequestParam Long type) {
        L.info("[GET] {}: find all medical order details", String.format("%s/medical-order/find-all?id=%d&type=%d", PMSConstants.PREFIX_URL, id, type));
        return ResponseEntity.ok().body(medicalOrderService.findAllMedicalOrderDetails(id, type));
    }

    @GetMapping(value = FIND_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicalOrderDetailsResponse> findById(@RequestParam Long id) {
        L.info("[GET] {}: find medical order details by id", PMSConstants.PREFIX_URL + "/medical-order/find-by-id?id=" + id);
        return ResponseEntity.ok().body(medicalOrderService.findMedicalOrderDetailById(id));
    }

    @GetMapping(value = FIND_BY_PATIENT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicalOrderResponse> findByPatient(@RequestParam Long id) {
        L.info("[GET] {}: find medical order details by id", PMSConstants.PREFIX_URL + "/medical-order/find-by-patient?id=" + id);
        return ResponseEntity.ok().body(medicalOrderService.findMedicalOrderByPatient(id));
    }
}
