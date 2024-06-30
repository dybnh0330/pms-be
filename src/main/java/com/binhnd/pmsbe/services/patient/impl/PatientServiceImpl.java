package com.binhnd.pmsbe.services.patient.impl;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.dto.request.SearchSortDto;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.PatientAdmissionResponse;
import com.binhnd.pmsbe.dto.response.PatientResponse;
import com.binhnd.pmsbe.entity.Department;
import com.binhnd.pmsbe.entity.Patient;
import com.binhnd.pmsbe.mapper.PatientMapper;
import com.binhnd.pmsbe.repositories.DepartmentRepository;
import com.binhnd.pmsbe.repositories.PatientRepository;
import com.binhnd.pmsbe.repositories.custom_repositories.CustomPatientRepository;
import com.binhnd.pmsbe.services.patient.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final CustomPatientRepository customPatientRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository,
                              CustomPatientRepository customPatientRepository,
                              DepartmentRepository departmentRepository) {
        this.patientRepository = patientRepository;
        this.customPatientRepository = customPatientRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public PatientResponse findPatientById(Long id) {

        Optional<Patient> patient = patientRepository.findById(id);

        if (patient.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_NOT_EXIST);
        }

        return PatientMapper.toDto(patient.get());
    }

    @Override
    public List<PatientResponse> findAllPatientByDepartment(Long departmentId) {

        Optional<Department> department = departmentRepository.findById(departmentId);
        if (department.isEmpty()) {
            throw new PMSException(EnumPMSException.DEPARTMENT_NOT_EXIST);
        }

        List<Patient> allPatientInDepartment = customPatientRepository.findAllPatientInDepartment(departmentId);

        return PatientMapper.mapAll(allPatientInDepartment);
    }

    @Override
    public List<PatientResponse> findAllPatient() {

        List<Patient> patients = patientRepository.findAll();

        return PatientMapper.mapAll(patients);
    }

    @Override
    public Page<PatientResponse> findAllPatientByDeparmentPage(Long departmentId, SearchSortPageableDTO dto) {

        Optional<Department> department = departmentRepository.findById(departmentId);
        if (department.isEmpty()) {
            throw new PMSException(EnumPMSException.DEPARTMENT_NOT_EXIST);
        }

        return customPatientRepository.findAllPatientInDepartmentPage(departmentId, dto);
    }

    @Override
    public List<PatientAdmissionResponse> findPatientByDepartmentIsNull(SearchSortDto dto) {

        List<Patient> patientByDepartmentIsNull = customPatientRepository.findPatientByDepartmentIsNull(dto);

        return PatientMapper.mapAllPatientAdmission(patientByDepartmentIsNull);
    }

    @Override
    public List<PatientResponse> findPatientByMedicalStaffIsNull(Long id) {

        Optional<Department> department = departmentRepository.findById(id);

        if (department.isEmpty()) {
            throw new PMSException(EnumPMSException.DEPARTMENT_NOT_EXIST);
        }

        List<Patient> findPatientByMedicalStaffIsNull = patientRepository.findPatientByMedicalStaffIsNullAndDepartmentIsNotNullAndDepartment(department.get());

        return PatientMapper.mapAll(findPatientByMedicalStaffIsNull);
    }
}
