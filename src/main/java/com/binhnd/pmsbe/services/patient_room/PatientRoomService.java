package com.binhnd.pmsbe.services.patient_room;

import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.PatientRoomResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PatientRoomService {
    List<PatientRoomResponse> findAllPatientRoomByDepartmentId(Long departmentId);

    List<PatientRoomResponse> findAllPatientRoomEmptyByDepartmentId(Long departmentId);

    Page<PatientRoomResponse> findAllPatientRoomByDepartmentPage(Long departmentId, SearchSortPageableDTO dto);

    PatientRoomResponse findPatientRoomById(Long id);
}
