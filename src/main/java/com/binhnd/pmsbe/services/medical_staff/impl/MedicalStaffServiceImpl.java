package com.binhnd.pmsbe.services.medical_staff.impl;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.MedicalStaffResponse;
import com.binhnd.pmsbe.entity.MedicalStaff;
import com.binhnd.pmsbe.mapper.MedicalStaffMapper;
import com.binhnd.pmsbe.repositories.MedicalStaffRepository;
import com.binhnd.pmsbe.repositories.custom_repositories.CustomMedicalStaffRepository;
import com.binhnd.pmsbe.services.medical_staff.MedicalStaffService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalStaffServiceImpl implements MedicalStaffService {

    private final MedicalStaffRepository medicalStaffRepository;

    private final CustomMedicalStaffRepository customMedicalStaffRepository;

    public MedicalStaffServiceImpl(MedicalStaffRepository medicalStaffRepository, CustomMedicalStaffRepository customMedicalStaffRepository) {
        this.medicalStaffRepository = medicalStaffRepository;
        this.customMedicalStaffRepository = customMedicalStaffRepository;
    }

    @Override
    public List<MedicalStaffResponse> findAllMedicalStaff() {
        return MedicalStaffMapper.mapAll(medicalStaffRepository.findAll());
    }

    @Override
    public List<MedicalStaffResponse> findMedicalStaffByAccountIsNull() {
        return MedicalStaffMapper.mapAll(medicalStaffRepository.findMedicalStaffByAccountIsNull());
    }

    @Override
    public List<MedicalStaffResponse> findAllMedicalStaffByDepartmentId(Long departmentId) {
        return MedicalStaffMapper.mapAll(customMedicalStaffRepository.findAllMedicalStaffByDepartmentId(departmentId));
    }

    @Override
    public List<MedicalStaffResponse> findAllDoctorByDepartment(Long departmentId) {
        return MedicalStaffMapper.mapAll(customMedicalStaffRepository.findAllDoctorByDepartment(departmentId));
    }

    @Override
    public MedicalStaffResponse findMedicalStaffById(Long id) {

        Optional<MedicalStaff> medicalStaffById = medicalStaffRepository.findById(id);

        if (medicalStaffById.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_STAFF_NOT_EXISTED);
        }

        return MedicalStaffMapper.toDto(medicalStaffById.get());
    }

    @Override
    public Page<MedicalStaffResponse> findAllMedicalStaffPage(SearchSortPageableDTO dto) {
        return customMedicalStaffRepository.findAllMedicalStaffPage(dto);
    }

    @Override
    public Page<MedicalStaffResponse> findAllMedicalStaffByDepartmentIdPage(Long departmentId, SearchSortPageableDTO dto) {
        return customMedicalStaffRepository.findAllMedicalStaffPageByDepartmentId(dto, departmentId);
    }
}
