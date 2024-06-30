package com.binhnd.pmsbe.services.patient_room;

import com.binhnd.pmsbe.dto.request.PatientRoomHasBedRequest;
import com.binhnd.pmsbe.dto.request.PatientRoomRequest;
import com.binhnd.pmsbe.dto.response.PatientRoomResponse;

public interface PatientRoomCUService {

    PatientRoomResponse createPatientRoom(PatientRoomHasBedRequest request);

    PatientRoomResponse updatePatientRoom(PatientRoomRequest request, Long id);

    void deletePatientRoom(Long id);


}
