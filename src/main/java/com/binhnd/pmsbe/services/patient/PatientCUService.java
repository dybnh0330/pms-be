package com.binhnd.pmsbe.services.patient;

import com.binhnd.pmsbe.dto.request.PatientInHospitalRequest;
import com.binhnd.pmsbe.dto.request.PatientInfoRequest;
import com.binhnd.pmsbe.dto.response.PatientResponse;

public interface PatientCUService {

    PatientResponse updateInfoPatient(PatientInfoRequest request, Long id);

    PatientResponse updatePatientInDepartment(PatientInHospitalRequest request, Long id);

    void deletePatient (Long id);

}
