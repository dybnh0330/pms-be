package com.binhnd.pmsbe.services.receive_patient.impl;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.common.utils.jwt.SecurityUtils;
import com.binhnd.pmsbe.dto.request.OrderDepartment;
import com.binhnd.pmsbe.dto.request.OrderMedicalOrder;
import com.binhnd.pmsbe.dto.request.PatientInfoRequest;
import com.binhnd.pmsbe.dto.request.ReceivePatientRequest;
import com.binhnd.pmsbe.entity.*;
import com.binhnd.pmsbe.mapper.PatientMapper;
import com.binhnd.pmsbe.repositories.*;
import com.binhnd.pmsbe.repositories.custom_repositories.CustomPatientRepository;
import com.binhnd.pmsbe.services.receive_patient.ReceivePatientService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.UUID;

import static com.binhnd.pmsbe.common.constants.PMSConstants.*;

@Service
public class ReceivePatientServiceImpl implements ReceivePatientService {

    private final PatientRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalOrderRepository medicalOrderRepository;
    private final MedicalOrderDetailsRepository medicalOrderDetailsRepository;
    private final DepartmentRepository departmentRepository;
    private final CategoryRepository categoryRepository;
    private final CustomPatientRepository customPatientRepository;

    @Autowired
    public ReceivePatientServiceImpl(PatientRepository patientRepository,
                                     MedicalRecordRepository medicalRecordRepository,
                                     MedicalOrderRepository medicalOrderRepository,
                                     MedicalOrderDetailsRepository medicalOrderDetailsRepository,
                                     DepartmentRepository departmentRepository,
                                     CategoryRepository categoryRepository, CustomPatientRepository customPatientRepository) {
        this.patientRepository = patientRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.medicalOrderRepository = medicalOrderRepository;
        this.medicalOrderDetailsRepository = medicalOrderDetailsRepository;
        this.departmentRepository = departmentRepository;
        this.categoryRepository = categoryRepository;
        this.customPatientRepository = customPatientRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receivePatient(ReceivePatientRequest request) {

        validateAndCorrectData(request);

        PatientInfoRequest patientRequest = PatientMapper.toPatientRequest(request);

        checkDuplicatePatient(patientRequest);

        Patient patient = new Patient();
        UUID patientCode = UUID.randomUUID();
        patient.setPatientCode(patientCode.toString());
        patient.setName(request.getPatientName());
        patient.setDateOfBirth(request.getDob());
        patient.setGender(request.getGender());
        patient.setAddress(request.getAddress());

        LocalDate currentDate = LocalDate.now();
        LocalDate birthDate = request.getDob().toLocalDateTime().toLocalDate();
        int age = Period.between(birthDate, currentDate).getYears();

        patient.setAge((long) age);
        patient.setBhytCode(request.getBhytCode());
        patient.setCccdNumber(request.getCccdNumber());
        patient.setPatientPhone(request.getPatientPhone());
        patient.setGuardianPhone(request.getGuardianPhone());
        patient.setAdmissionTime(Timestamp.from(Instant.now()));
        patient.setStatus(HOSPITALIZATION);
        patient.setCreateBy(SecurityUtils.getCurrentUsername());

        Patient newPatient = patientRepository.save(patient);

        MedicalRecord medicalRecord = new MedicalRecord();
        UUID recordCode = UUID.randomUUID();
        medicalRecord.setTitle(RECORD_TITLE + patientCode);
        medicalRecord.setRecordCode(recordCode.toString());
        medicalRecord.setReason(request.getReason());
        medicalRecord.setMedicalHistory(request.getMedicalHistory());
        medicalRecord.setCreateTime(Timestamp.from(Instant.now()));
        medicalRecord.setCreateBy(SecurityUtils.getCurrentUsername());
        medicalRecord.setPatient(newPatient);

        medicalRecordRepository.save(medicalRecord);

        MedicalOrder medicalOrder = new MedicalOrder();
        medicalOrder.setTitle(ORDER_TITLE + patientCode);
        medicalOrder.setPatient(newPatient);

        medicalOrderRepository.save(medicalOrder);
    }

    @Override
    public void orderDepartment(Long id, OrderDepartment request) {
        Optional<Department> department = departmentRepository.findById(request.getDepartmentId());
        if (department.isEmpty()) {
            throw new PMSException(EnumPMSException.DEPARTMENT_NOT_EXIST);
        }

        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_NOT_EXIST);
        }

        Patient patientRequest = patient.get();
        MedicalRecord medicalRecordByPatient = medicalRecordRepository.findMedicalRecordByPatient(patientRequest);
        if (ObjectUtils.isEmpty(medicalRecordByPatient)) {
            throw new PMSException(EnumPMSException.MEDICAL_RECORD_NOT_EXISTED);
        }

        medicalRecordByPatient.setDiagnostic(request.getDiagnostic());
        medicalRecordByPatient.setUpdateTime(Timestamp.from(Instant.now()));
        medicalRecordByPatient.setUpdateBy("admin");

        medicalRecordRepository.save(medicalRecordByPatient);

        patientRequest.setDepartment(department.get());
        patientRepository.save(patientRequest);
    }

    @Override
    public void orderMedicalOrder(Long id, OrderMedicalOrder request) {

        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_NOT_EXIST);
        }
        MedicalOrder medicalOrder = patient.get().getMedicalOrder();

        if (ObjectUtils.isEmpty(medicalOrder)) {
            throw new PMSException(EnumPMSException.MEDICAL_ORDER_NOT_EXISTED);
        }

        if (!request.getMedicalOrderIds().isEmpty() && !ObjectUtils.isEmpty(request.getMedicalOrderIds())) {
            for (Long orderId : request.getMedicalOrderIds()) {

                Optional<Category> category = categoryRepository.findById(orderId);
                if (category.isEmpty()) {
                    throw new PMSException(EnumPMSException.CATEGORY_NOT_EXISTED);
                }

                MedicalOrderDetails medicalOrderDetails = new MedicalOrderDetails();
                medicalOrderDetails.setType(TEST_ORDER);
                medicalOrderDetails.setQuantity(QUANTITY);
                medicalOrderDetails.setUnit(UNIT);
                medicalOrderDetails.setNote(NOTE);
                medicalOrderDetails.setCategory(category.get());
                medicalOrderDetails.setMedicalOrder(medicalOrder);
                medicalOrderDetails.setCreateTime(Timestamp.from(Instant.now()));
                medicalOrderDetails.setCreateBy("admin");

                medicalOrderDetailsRepository.save(medicalOrderDetails);
            }
        }
    }

    private void validateAndCorrectData(ReceivePatientRequest request) {
        if (ObjectUtils.isEmpty(request)
                || ObjectUtils.isEmpty(request.getPatientName())
                || ObjectUtils.isEmpty(request.getDob())
                || ObjectUtils.isEmpty(request.getGender())
                || ObjectUtils.isEmpty(request.getReason())
                || ObjectUtils.isEmpty(request.getAddress())
                || ObjectUtils.isEmpty(request.getMedicalHistory())
                || ObjectUtils.isEmpty(request.getCccdNumber())) {
            throw new PMSException(EnumPMSException.RECEIVE_PATIENT_DATA_INVALID);
        }

        request.setPatientName(StringUtil.removeWhitespace(request.getPatientName()));
        request.setReason(StringUtil.removeWhitespace(request.getReason()));
        request.setMedicalHistory(StringUtil.removeWhitespace(request.getMedicalHistory()));
        request.setCccdNumber(StringUtil.removeWhitespace(request.getCccdNumber()));
        request.setBhytCode(StringUtil.removeWhitespace(request.getBhytCode()));
        request.setPatientPhone(StringUtil.removeWhitespace(request.getPatientPhone()));
        request.setAddress(StringUtil.removeWhitespace(request.getAddress()));
        request.setGuardianPhone(StringUtil.removeWhitespace(request.getGuardianPhone()));
    }

    private void checkDuplicatePatient(PatientInfoRequest request) {
        Patient patient = customPatientRepository.findPatientExisted(request);
        if (!ObjectUtils.isEmpty(patient)) {
            throw new PMSException(EnumPMSException.PATIENT_EXISTED);
        }
    }
}
