package com.binhnd.pmsbe.services.medical_order;

import com.binhnd.pmsbe.dto.response.MedicalOrderDetailsResponse;
import com.binhnd.pmsbe.dto.response.MedicalOrderResponse;

import java.util.List;

public interface MedicalOrderService {

    MedicalOrderDetailsResponse findMedicalOrderDetailById(Long id);

    List<MedicalOrderDetailsResponse> findAllMedicalOrderDetails(Long medicalOrderId, Long type);

    MedicalOrderResponse findMedicalOrderByPatient(Long patientId);

}
