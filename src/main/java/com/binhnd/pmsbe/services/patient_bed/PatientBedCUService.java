package com.binhnd.pmsbe.services.patient_bed;

import com.binhnd.pmsbe.dto.request.PatientBedRequest;
import com.binhnd.pmsbe.dto.response.PatientBedResponse;
import com.binhnd.pmsbe.entity.PatientBed;

import java.util.List;

public interface PatientBedCUService {

    PatientBedResponse createPatientBed(PatientBedRequest request);

    PatientBedResponse updatePatientBed(PatientBedRequest request, Long id);

    void deletePatientBed(Long id);

    void deleteAllPatientBed(Long roomId);

}
