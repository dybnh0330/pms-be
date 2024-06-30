package com.binhnd.pmsbe.services.medical_staff.impl;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.common.utils.ObjectMapperUtils;
import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.common.utils.jwt.SecurityUtils;
import com.binhnd.pmsbe.dto.request.MedicalStaffRequest;
import com.binhnd.pmsbe.dto.response.MedicalStaffResponse;
import com.binhnd.pmsbe.common.enums.Account;
import com.binhnd.pmsbe.entity.Department;
import com.binhnd.pmsbe.entity.MedicalStaff;
import com.binhnd.pmsbe.entity.Patient;
import com.binhnd.pmsbe.mapper.MedicalStaffMapper;
import com.binhnd.pmsbe.repositories.DepartmentRepository;
import com.binhnd.pmsbe.repositories.MedicalStaffRepository;
import com.binhnd.pmsbe.repositories.custom_repositories.CustomMedicalStaffRepository;
import com.binhnd.pmsbe.services.medical_staff.MedicalStaffCUService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalStaffCUServiceImpl implements MedicalStaffCUService {

    private final MedicalStaffRepository medicalStaffRepository;

    private final CustomMedicalStaffRepository customMedicalStaffRepository;

    private final DepartmentRepository departmentRepository;

    @Autowired
    public MedicalStaffCUServiceImpl(MedicalStaffRepository medicalStaffRepository,
                                     CustomMedicalStaffRepository customMedicalStaffRepository,
                                     DepartmentRepository departmentRepository) {
        this.medicalStaffRepository = medicalStaffRepository;
        this.customMedicalStaffRepository = customMedicalStaffRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public MedicalStaffResponse createMedicalStaff(MedicalStaffRequest request) {

        validateAndCorrectDataMedicalStaff(request);

        checkDuplicateMedicalStaff(request, null);

        Optional<Department> departmentById = departmentRepository.findById(request.getDepartmentId());

        if (departmentById.isEmpty()) {
            throw new PMSException(EnumPMSException.DEPARTMENT_NOT_EXIST);
        }

        MedicalStaff medicalStaff = ObjectMapperUtils.map(request, MedicalStaff.class);

        medicalStaff.setDepartment(departmentById.get());
        medicalStaff.setCreateBy(SecurityUtils.getCurrentUsername());
        medicalStaff.setCreateTime(Timestamp.from(Instant.now()));
        medicalStaff.setUpdateTime(Timestamp.from(Instant.now()));

        medicalStaffRepository.save(medicalStaff);

        return MedicalStaffMapper.toDto(medicalStaff);
    }

    @Override
    public MedicalStaffResponse updateMedicalStaff(MedicalStaffRequest request, Long id) {

        validateAndCorrectDataMedicalStaff(request);

        checkDuplicateMedicalStaff(request, id);

        Optional<MedicalStaff> medicalStaffById = medicalStaffRepository.findById(id);
        if (medicalStaffById.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_STAFF_NOT_EXISTED);
        }

        Optional<Department> departmentById = departmentRepository.findById(request.getDepartmentId());
        if (departmentById.isEmpty()) {
            throw new PMSException(EnumPMSException.DEPARTMENT_NOT_EXIST);
        }

        MedicalStaff updateStaff = medicalStaffById.get();

        updateStaff.setName(request.getName());
        updateStaff.setDob(request.getDob());
        updateStaff.setGender(request.getGender());
        updateStaff.setCertificate(request.getCertificate());
        updateStaff.setCccd(request.getCccd());
        updateStaff.setPhoneNumber(request.getPhoneNumber());
        updateStaff.setAddress(request.getAddress());
        updateStaff.setEmail(request.getEmail());
        updateStaff.setSpecialize(request.getSpecialize());
        updateStaff.setDepartment(departmentById.get());
        updateStaff.setUpdateTime(Timestamp.from(Instant.now()));
        updateStaff.setUpdateBy(SecurityUtils.getCurrentUsername());

        medicalStaffRepository.save(updateStaff);

        return MedicalStaffMapper.toDto(updateStaff);
    }

    @Override
    public void deleteMedicalStaff(Long id) {

        Optional<MedicalStaff> medicalStaff = medicalStaffRepository.findById(id);

        if (medicalStaff.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_STAFF_NOT_EXISTED);
        }

        MedicalStaff staff = medicalStaff.get();
        List<Patient> patients = staff.getPatients();
        Account account = staff.getAccount();

        if (!patients.isEmpty()) {
            throw new PMSException(EnumPMSException.DELETE_MEDICAL_STAFF_FAIL);
        }

//        if (!ObjectUtils.isEmpty(account)) {
//            throw new PMSException(EnumPMSException.STAFF_HAS_ACCOUNT);
//        }

        medicalStaffRepository.deleteById(id);
    }

    private void validateAndCorrectDataMedicalStaff(MedicalStaffRequest request) {
        if (request == null
                || ObjectUtils.isEmpty(request.getName())
                || ObjectUtils.isEmpty(request.getDepartmentId())
                || ObjectUtils.isEmpty(request.getSpecialize())
                || ObjectUtils.isEmpty(request.getCccd())
                || ObjectUtils.isEmpty(request.getEmail())
                || ObjectUtils.isEmpty(request.getGender())
                || ObjectUtils.isEmpty(request.getAddress())
                || ObjectUtils.isEmpty(request.getDob())
                || ObjectUtils.isEmpty(request.getPhoneNumber())
                || ObjectUtils.isEmpty(request.getCertificate())
        ){
            throw new PMSException(EnumPMSException.MEDICAL_STAFF_INVALID);
        }

        request.setName(StringUtil.removeWhitespace(request.getName()));
        request.setCccd(StringUtil.removeWhitespace(request.getCccd()));
        request.setEmail(StringUtil.removeWhitespace(request.getEmail()));
        request.setAddress(StringUtil.removeWhitespace(request.getAddress()));
        request.setPhoneNumber(StringUtil.removeWhitespace(request.getPhoneNumber()));
        request.setSpecialize(StringUtil.removeWhitespace(request.getSpecialize()));
        request.setCertificate(StringUtil.removeWhitespace(request.getCertificate()));
    }

    private void checkDuplicateMedicalStaff(MedicalStaffRequest request, Long id) {
        MedicalStaff medicalStaff = customMedicalStaffRepository.findMedicalStaffExisted(request);
        if (!ObjectUtils.isEmpty(medicalStaff) && (ObjectUtils.isEmpty(id) || !medicalStaff.getId().equals(id))) {
            throw new PMSException(EnumPMSException.MEDICAL_STAFF_EXISTED);
        }
    }
}
