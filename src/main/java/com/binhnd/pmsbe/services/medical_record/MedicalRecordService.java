package com.binhnd.pmsbe.services.medical_record;

import com.binhnd.pmsbe.dto.response.MedicalRecordDetailResponse;
import com.binhnd.pmsbe.dto.response.MedicalRecordResponse;

import java.util.List;

public interface MedicalRecordService {

    MedicalRecordResponse findMedicalRecordByPatientId(Long id);

    List<MedicalRecordDetailResponse> findAllMedicalRecordDetail(Long recordId);

    MedicalRecordResponse findById(Long id);

    MedicalRecordDetailResponse findMedicalRecordDetailById(Long id);

}
