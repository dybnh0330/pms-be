package com.binhnd.pmsbe.services.patient;

import com.binhnd.pmsbe.dto.request.SearchSortDto;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.PatientAdmissionResponse;
import com.binhnd.pmsbe.dto.response.PatientResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PatientService {

    PatientResponse findPatientById(Long id);

    List<PatientResponse> findAllPatientByDepartment(Long departmentId);

    List<PatientResponse> findAllPatient();

    Page<PatientResponse> findAllPatientByDeparmentPage(Long departmentId, SearchSortPageableDTO dto);

    List<PatientAdmissionResponse> findPatientByDepartmentIsNull(SearchSortDto dto);

    List<PatientResponse> findPatientByMedicalStaffIsNull(Long id);
}
