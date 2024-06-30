package com.binhnd.pmsbe.services.medical_staff;

import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.MedicalStaffResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MedicalStaffService {

    List<MedicalStaffResponse> findAllMedicalStaff();

    List<MedicalStaffResponse> findMedicalStaffByAccountIsNull();

    List<MedicalStaffResponse> findAllMedicalStaffByDepartmentId(Long departmentId);

    List<MedicalStaffResponse> findAllDoctorByDepartment(Long departmentId);

    MedicalStaffResponse findMedicalStaffById(Long id);

    Page<MedicalStaffResponse> findAllMedicalStaffPage(SearchSortPageableDTO dto);

    Page<MedicalStaffResponse> findAllMedicalStaffByDepartmentIdPage(Long departmentId, SearchSortPageableDTO dto);

}
