package com.binhnd.pmsbe.services.receive_patient;

import com.binhnd.pmsbe.dto.request.OrderDepartment;
import com.binhnd.pmsbe.dto.request.OrderMedicalOrder;
import com.binhnd.pmsbe.dto.request.ReceivePatientRequest;

public interface ReceivePatientService {

    void receivePatient(ReceivePatientRequest request);

    void orderDepartment(Long id, OrderDepartment request);

    void orderMedicalOrder(Long id, OrderMedicalOrder request);
}
