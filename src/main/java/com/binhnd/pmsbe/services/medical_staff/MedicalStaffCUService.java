package com.binhnd.pmsbe.services.medical_staff;

import com.binhnd.pmsbe.dto.request.MedicalStaffRequest;
import com.binhnd.pmsbe.dto.response.MedicalStaffResponse;
import org.springframework.stereotype.Service;

public interface MedicalStaffCUService {

    MedicalStaffResponse createMedicalStaff(MedicalStaffRequest request);
    MedicalStaffResponse updateMedicalStaff(MedicalStaffRequest request, Long id);
    void deleteMedicalStaff(Long id);
}
