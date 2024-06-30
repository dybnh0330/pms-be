package com.binhnd.pmsbe.services.patient_bed;

import com.binhnd.pmsbe.dto.response.PatientBedResponse;
import com.binhnd.pmsbe.entity.PatientBed;

import java.util.List;

public interface PatientBedService {

    List<PatientBedResponse> findAllBedByRoomId(Long roomId);

    List<PatientBedResponse> findAllBedEmptyByRoom(Long roomId);

    PatientBedResponse findPatientBedById(Long id);

    Boolean checkDuplicatePatientBed(PatientBed request, Long id);
}
