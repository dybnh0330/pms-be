package com.binhnd.pmsbe.services.medical_record;

import com.binhnd.pmsbe.dto.request.MedicalRecordDetailRequest;
import com.binhnd.pmsbe.dto.request.MedicalRecordRequest;
import com.binhnd.pmsbe.dto.response.MedicalRecordDetailResponse;
import com.binhnd.pmsbe.dto.response.MedicalRecordResponse;

public interface MedicalRecordCUService {

    MedicalRecordResponse updateRecord(Long id, MedicalRecordRequest request);

    void createRecordDetail(MedicalRecordDetailRequest request);

    void updateRecordDetail(Long id, MedicalRecordDetailRequest request);

    void deleteRecordDetail(Long id);

}
