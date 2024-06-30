package com.binhnd.pmsbe.services.medical_record.impl;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.common.utils.jwt.SecurityUtils;
import com.binhnd.pmsbe.dto.request.MedicalRecordDetailRequest;
import com.binhnd.pmsbe.dto.request.MedicalRecordRequest;
import com.binhnd.pmsbe.dto.response.MedicalRecordResponse;
import com.binhnd.pmsbe.entity.MedicalRecord;
import com.binhnd.pmsbe.entity.MedicalRecordDetails;
import com.binhnd.pmsbe.mapper.MedicalRecordMapper;
import com.binhnd.pmsbe.repositories.MedicalRecordDetailsRepository;
import com.binhnd.pmsbe.repositories.MedicalRecordRepository;
import com.binhnd.pmsbe.services.medical_record.MedicalRecordCUService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class MedicalRecordCUServiceImpl implements MedicalRecordCUService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordDetailsRepository medicalRecordDetailsRepository;

    @Autowired
    public MedicalRecordCUServiceImpl(MedicalRecordRepository medicalRecordRepository,
                                      MedicalRecordDetailsRepository medicalRecordDetailsRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.medicalRecordDetailsRepository = medicalRecordDetailsRepository;
    }

    @Override
    public MedicalRecordResponse updateRecord(Long id, MedicalRecordRequest request) {

        validateAndCorrectData(request);

        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(id);
        if (medicalRecord.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_RECORD_NOT_EXISTED);
        }

        MedicalRecord updateRecord = medicalRecord.get();
        updateRecord.setReason(request.getReason());
        updateRecord.setMedicalHistory(request.getMedicalHistory());
        updateRecord.setDiagnostic(request.getDiagnostic());
        updateRecord.setUpdateTime(Timestamp.from(Instant.now()));
        updateRecord.setUpdateBy(SecurityUtils.getCurrentUsername());

        medicalRecordRepository.save(updateRecord);

        return MedicalRecordMapper.toDto(updateRecord);
    }

    @Override
    public void createRecordDetail(MedicalRecordDetailRequest request) {

        validateAndCorrectData(request);

        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(request.getRecordId());
        if (medicalRecord.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_RECORD_NOT_EXISTED);
        }

        MedicalRecordDetails newRecordDetails = new MedicalRecordDetails();
        newRecordDetails.setTitle(request.getTitle());
        newRecordDetails.setDescription(request.getDescription());
        newRecordDetails.setMedicalRecord(medicalRecord.get());
        newRecordDetails.setCreateTime(Timestamp.from(Instant.now()));
        newRecordDetails.setCreateBy(SecurityUtils.getCurrentUsername());

        medicalRecordDetailsRepository.save(newRecordDetails);
    }

    @Override
    public void updateRecordDetail(Long id, MedicalRecordDetailRequest request) {

        validateAndCorrectData(request);

        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(request.getRecordId());
        if (medicalRecord.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_RECORD_NOT_EXISTED);
        }

        Optional<MedicalRecordDetails> details = medicalRecordDetailsRepository.findById(id);
        if (details.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_RECORD_NOT_EXISTED);
        }

        if (!request.getRecordId().equals(details.get().getMedicalRecord().getId())) {
            throw new PMSException(EnumPMSException.MEDICAL_RECORD_INCORRECT);
        }

        MedicalRecordDetails medicalRecordDetails = details.get();
        medicalRecordDetails.setTitle(request.getTitle());
        medicalRecordDetails.setDescription(request.getDescription());
        medicalRecordDetails.setUpdateTime(Timestamp.from(Instant.now()));
        medicalRecordDetails.setUpdateBy(SecurityUtils.getCurrentUsername());

        medicalRecordDetailsRepository.save(medicalRecordDetails);
    }

    @Override
    public void deleteRecordDetail(Long id) {

        Optional<MedicalRecordDetails> recordDetails = medicalRecordDetailsRepository.findById(id);
        if (recordDetails.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_RECORD_NOT_EXISTED);
        }

        medicalRecordDetailsRepository.delete(recordDetails.get());
    }

    private void validateAndCorrectData(MedicalRecordRequest request) {
        if (ObjectUtils.isEmpty(request)
                || ObjectUtils.isEmpty(request.getReason())
                || ObjectUtils.isEmpty(request.getDiagnostic())) {
            throw new PMSException(EnumPMSException.MEDICAL_RECORD_DATA_INVALID);
        }

        request.setReason(StringUtil.removeWhitespace(request.getReason()));
        request.setDiagnostic(StringUtil.removeWhitespace(request.getDiagnostic()));
        request.setMedicalHistory(StringUtil.removeWhitespace(request.getMedicalHistory()));
    }

    private void validateAndCorrectData(MedicalRecordDetailRequest request) {
        if (ObjectUtils.isEmpty(request)
                || ObjectUtils.isEmpty(request.getDescription())
                || ObjectUtils.isEmpty(request.getTitle())) {
            throw new PMSException(EnumPMSException.MEDICAL_RECORD_DATA_INVALID);
        }

        request.setTitle(StringUtil.removeWhitespace(request.getTitle()));
        request.setDescription(StringUtil.removeWhitespace(request.getDescription()));
    }
}
