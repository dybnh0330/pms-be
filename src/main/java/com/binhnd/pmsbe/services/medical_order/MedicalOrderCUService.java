package com.binhnd.pmsbe.services.medical_order;

import com.binhnd.pmsbe.dto.request.MedicalOrderRequest;
import com.binhnd.pmsbe.dto.response.MedicalOrderDetailsResponse;

import java.util.List;

public interface MedicalOrderCUService {

    void addMedicalOrderDetail(List<MedicalOrderRequest> request);

    void cancelMedicalOrderDetails(Long id);

}
