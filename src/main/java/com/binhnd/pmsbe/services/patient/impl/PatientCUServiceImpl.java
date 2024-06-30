package com.binhnd.pmsbe.services.patient.impl;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.common.utils.jwt.SecurityUtils;
import com.binhnd.pmsbe.dto.request.PatientInHospitalRequest;
import com.binhnd.pmsbe.dto.request.PatientInfoRequest;
import com.binhnd.pmsbe.dto.response.PatientResponse;
import com.binhnd.pmsbe.entity.*;
import com.binhnd.pmsbe.mapper.PatientMapper;
import com.binhnd.pmsbe.repositories.*;
import com.binhnd.pmsbe.services.patient.PatientCUService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PatientCUServiceImpl implements PatientCUService {

    private final PatientRepository patientRepository;
    private final MedicalStaffRepository medicalStaffRepository;
    private final PatientRoomRepository patientRoomRepository;
    private final DepartmentRepository departmentRepository;
    private final PatientBedRepository patientBedRepository;

    @Autowired
    public PatientCUServiceImpl(PatientRepository patientRepository,
                                MedicalStaffRepository medicalStaffRepository,
                                PatientRoomRepository patientRoomRepository,
                                DepartmentRepository departmentRepository,
                                PatientBedRepository patientBedRepository) {
        this.patientRepository = patientRepository;
        this.medicalStaffRepository = medicalStaffRepository;
        this.patientRoomRepository = patientRoomRepository;
        this.departmentRepository = departmentRepository;
        this.patientBedRepository = patientBedRepository;
    }


    @Override
    public PatientResponse updateInfoPatient(PatientInfoRequest request, Long id) {

        validateAndCorrectData(request);

        checkDuplicatePatient(request, id);

        Optional<Patient> patientExisted = patientRepository.findById(id);
        if (patientExisted.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_NOT_EXIST);
        }

        Patient patient = patientExisted.get();
        if (patient.getStatus().equals(PMSConstants.HOSPITALIZATION)) {
            throw new PMSException(EnumPMSException.UPDATE_INFO_FAIL);
        }

        patient.setName(request.getPatientName());
        patient.setDateOfBirth(request.getDob());
        patient.setGender(request.getGender());
        patient.setBhytCode(request.getBhytCode());
        patient.setCccdNumber(request.getCccdNumber());
        patient.setPatientPhone(request.getPatientPhone());
        patient.setGuardianPhone(request.getGuardianPhone());
        patient.setUpdateTime(Timestamp.from(Instant.now()));
        patient.setStatus(PMSConstants.UNDER_TREATMENT);
        patient.setUpdateBy(SecurityUtils.getCurrentUsername());

        Patient response = patientRepository.save(patient);

        return PatientMapper.toDto(response);
    }

    @Override
    public PatientResponse updatePatientInDepartment(PatientInHospitalRequest request, Long id) {

        Optional<Patient> patientExisted = patientRepository.findById(id);
        Optional<MedicalStaff> medicalStaff = medicalStaffRepository.findById(request.getMedicalStaffId());
        Optional<PatientRoom> patientRoom = patientRoomRepository.findById(request.getRoomId());
        Optional<PatientBed> bed = patientBedRepository.findById(request.getPatientBedId());
        Optional<Department> department = departmentRepository.findById(request.getDepartmentId());

        if (patientExisted.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_NOT_EXIST);
        }
        if (department.isEmpty()) {
            throw new PMSException(EnumPMSException.DEPARTMENT_NOT_EXIST);
        }
        if (medicalStaff.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_STAFF_NOT_EXISTED);
        }
        if (patientRoom.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_ROOM_NOT_EXISTED);
        }
        if (bed.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_BED_NOT_EXISTED);
        }

        Patient patient = patientExisted.get();
        List<MedicalStaff> medicalStaffs = patient.getDepartment().getMedicalStaffs();
        if (!medicalStaffs.contains(medicalStaff.get())) {
            throw new PMSException(EnumPMSException.UPDATE_MEDICAL_STAFF_FAIL);
        }

        if (!medicalStaff.get().getSpecialize().equalsIgnoreCase("Bác sĩ")) {
            throw new PMSException(EnumPMSException.MEDICAL_STAFF_NOT_DOCTOR);
        }

        List<PatientRoom> patientRooms = patient.getDepartment().getPatientRooms();
        PatientRoom room = patientRoom.get();
        if (room.getStatus().equals(Boolean.FALSE)) {
            throw new PMSException(EnumPMSException.ROOM_NOT_EMPTY);
        }

        if (!patientRooms.contains(room)) {
            throw new PMSException(EnumPMSException.UPDATE_ROOM_FAIL);
        }

        List<PatientBed> patientBeds = room.getBeds();
        PatientBed patientBed = bed.get();
        if (patientBed.getStatus().equals(Boolean.FALSE)
                && (ObjectUtils.isEmpty(patientBed.getPatient()) || !patientBed.getPatient().equals(patient))) {
            throw new PMSException(EnumPMSException.BED_NOT_EMPTY);
        }

        if (!patientBeds.contains(patientBed)) {
            throw new PMSException(EnumPMSException.UPDATE_BED_FAIL);
        }

        patient.setMedicalStaff(medicalStaff.get());
        patient.setPatientBed(patientBed);
        patient.setStatus(PMSConstants.UNDER_TREATMENT);
        patient.setUpdateTime(Timestamp.from(Instant.now()));
        patient.setUpdateBy(SecurityUtils.getCurrentUsername());

        patientBed.setPatient(patient);
        patientBed.setStatus(Boolean.FALSE);
        patientBedRepository.save(patientBed);

        patientRepository.save(patient);

        return PatientMapper.toDto(patient);
    }

    @Override
    public void deletePatient(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_NOT_EXIST);
        }
        patientRepository.deleteById(id);
    }

    private void validateAndCorrectData(PatientInfoRequest request) {
        if (ObjectUtils.isEmpty(request)
                || ObjectUtils.isEmpty(request.getPatientPhone())
                || ObjectUtils.isEmpty(request.getGender())
                || ObjectUtils.isEmpty(request.getDob())
                || ObjectUtils.isEmpty(request.getPatientName())) {
            throw new PMSException(EnumPMSException.PATIENT_DATA_INVALID);
        }
        request.setPatientName(StringUtil.removeWhitespace(request.getPatientName()));
        request.setCccdNumber(StringUtil.removeWhitespace(request.getCccdNumber()));
        request.setPatientPhone(StringUtil.removeWhitespace(request.getPatientPhone()));
        request.setGuardianPhone(StringUtil.removeWhitespace(request.getGuardianPhone()));
        request.setBhytCode(StringUtil.removeWhitespace(request.getBhytCode()));
    }

    private void checkDuplicatePatient(PatientInfoRequest request, Long id) {
        Patient patient = patientRepository.findPatientByBhytCodeAndCccdNumberAndPatientPhone(
                request.getBhytCode(),
                request.getCccdNumber(),
                request.getPatientPhone());
        if (!ObjectUtils.isEmpty(patient) && (ObjectUtils.isEmpty(id) || !patient.getId().equals(id))) {
            throw new PMSException(EnumPMSException.PATIENT_EXISTED);
        }
    }
}
