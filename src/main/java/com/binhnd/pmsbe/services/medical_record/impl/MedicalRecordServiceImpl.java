package com.binhnd.pmsbe.services.medical_record.impl;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.dto.response.MedicalRecordDetailResponse;
import com.binhnd.pmsbe.dto.response.MedicalRecordResponse;
import com.binhnd.pmsbe.entity.MedicalOrderDetails;
import com.binhnd.pmsbe.entity.MedicalRecord;
import com.binhnd.pmsbe.entity.MedicalRecordDetails;
import com.binhnd.pmsbe.entity.Patient;
import com.binhnd.pmsbe.mapper.MedicalRecordMapper;
import com.binhnd.pmsbe.repositories.MedicalRecordDetailsRepository;
import com.binhnd.pmsbe.repositories.MedicalRecordRepository;
import com.binhnd.pmsbe.repositories.PatientRepository;
import com.binhnd.pmsbe.services.medical_record.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final MedicalRecordDetailsRepository medicalRecordDetailsRepository;

    @Autowired
    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository,
                                    PatientRepository patientRepository,
                                    MedicalRecordDetailsRepository medicalRecordDetailsRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
        this.medicalRecordDetailsRepository = medicalRecordDetailsRepository;
    }

    @Override
    public MedicalRecordResponse findMedicalRecordByPatientId(Long id) {

        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_NOT_EXIST);
        }

        MedicalRecord medicalRecordByPatient = medicalRecordRepository.findMedicalRecordByPatient(patient.get());

        return MedicalRecordMapper.toDto(medicalRecordByPatient);
    }

    @Override
    public List<MedicalRecordDetailResponse> findAllMedicalRecordDetail(Long recordId) {

        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(recordId);
        if (medicalRecord.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_RECORD_NOT_EXISTED);
        }

        List<MedicalRecordDetails> medicalOrderDetails = medicalRecordDetailsRepository.findMedicalRecordDetailsByMedicalRecord(medicalRecord.get());

        return MedicalRecordMapper.mapAll(medicalOrderDetails);
    }

    @Override
    public MedicalRecordResponse findById(Long id) {

        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(id);
        if (medicalRecord.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_RECORD_NOT_EXISTED);
        }

        return MedicalRecordMapper.toDto(medicalRecord.get());
    }

    @Override
    public MedicalRecordDetailResponse findMedicalRecordDetailById(Long id) {

        Optional<MedicalRecordDetails> medicalRecordDetails = medicalRecordDetailsRepository.findById(id);
        if (medicalRecordDetails.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_RECORD_NOT_EXISTED);
        }

        return MedicalRecordMapper.toDto(medicalRecordDetails.get());
    }
}
