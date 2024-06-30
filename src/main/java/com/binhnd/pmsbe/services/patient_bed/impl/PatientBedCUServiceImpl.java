package com.binhnd.pmsbe.services.patient_bed.impl;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.common.utils.ObjectMapperUtils;
import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.common.utils.jwt.SecurityUtils;
import com.binhnd.pmsbe.dto.request.PatientBedRequest;
import com.binhnd.pmsbe.dto.response.PatientBedResponse;
import com.binhnd.pmsbe.entity.PatientBed;
import com.binhnd.pmsbe.entity.PatientRoom;
import com.binhnd.pmsbe.mapper.PatientBedMapper;
import com.binhnd.pmsbe.repositories.PatientBedRepository;
import com.binhnd.pmsbe.repositories.PatientRoomRepository;
import com.binhnd.pmsbe.repositories.custom_repositories.CustomPatientBedRepository;
import com.binhnd.pmsbe.services.patient_bed.PatientBedCUService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientBedCUServiceImpl implements PatientBedCUService {

    private final PatientBedRepository patientBedRepository;
    private final PatientRoomRepository patientRoomRepository;
    private final CustomPatientBedRepository customPatientBedRepository;

    @Autowired
    public PatientBedCUServiceImpl(PatientBedRepository patientBedRepository,
                                   PatientRoomRepository patientRoomRepository,
                                   CustomPatientBedRepository customPatientBedRepository) {
        this.patientBedRepository = patientBedRepository;
        this.patientRoomRepository = patientRoomRepository;
        this.customPatientBedRepository = customPatientBedRepository;
    }

    @Override
    public PatientBedResponse createPatientBed(PatientBedRequest request) {

        validateAndCorrectData(request);

        checkDuplicatePatientBed(request, null);

        Optional<PatientRoom> patientRoom = patientRoomRepository.findById(request.getRoomId());

        if (patientRoom.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_ROOM_NOT_EXISTED);
        }

        PatientRoom patientRoomHasBeds = patientRoom.get();
        List<PatientBed> patientBedsInRoom = patientRoomHasBeds.getBeds();

        Long totalBeds = (long) patientBedsInRoom.size();

        if (patientRoomHasBeds.getTotalBed().equals(totalBeds)) {
            throw new PMSException(EnumPMSException.CREATE_PATIENT_BED_FAIL);
        }

        UUID bedCode= UUID.randomUUID();

        PatientBed patientBed = ObjectMapperUtils.map(request, PatientBed.class);
        patientBed.setBedCode(bedCode.toString());
        patientBed.setStatus(patientRoomHasBeds.getStatus());
        patientBed.setCreateBy(SecurityUtils.getCurrentUsername());
        patientBed.setCreateTime(Timestamp.from(Instant.now()));
        patientBed.setPatientRoom(patientRoomHasBeds);

        patientBedRepository.save(patientBed);

        return PatientBedMapper.toDto(patientBed);
    }

    @Override
    public PatientBedResponse updatePatientBed(PatientBedRequest request, Long id) {

        validateAndCorrectData(request);

        checkDuplicatePatientBed(request, id);

        Optional<PatientRoom> patientRoom = patientRoomRepository.findById(request.getRoomId());
        if (patientRoom.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_ROOM_NOT_EXISTED);
        }

        List<PatientBed> patientBedsInRoom = patientRoom.get().getBeds();


        Optional<PatientBed> patientBed = patientBedRepository.findById(id);
        if (patientBed.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_BED_NOT_EXISTED);
        }

        if (!patientBedsInRoom.contains(patientBed.get())) {
            throw new PMSException(EnumPMSException.PATIENT_BED_NOT_IN_ROOM);
        }

        PatientBed patientBedRequest = patientBed.get();
        patientBedRequest.setBedNumber(request.getBedNumber());
        patientBedRequest.setStatus(request.getStatus());
        patientBedRequest.setPatientRoom(patientRoom.get());
        patientBedRequest.setUpdateBy(SecurityUtils.getCurrentUsername());
        patientBedRequest.setUpdateTime(Timestamp.from(Instant.now()));

        patientBedRepository.save(patientBedRequest);

        return PatientBedMapper.toDto(patientBedRequest);
    }

    @Override
    public void deletePatientBed(Long id) {
        Optional<PatientBed> patientBed = patientBedRepository.findById(id);

        if (patientBed.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_BED_NOT_EXISTED);
        }

        if (!ObjectUtils.isEmpty(patientBed.get().getPatient())) {
            throw new PMSException(EnumPMSException.DELETE_PATIENT_BED_FAIL);
        }

        patientBedRepository.deleteById(id);
    }

    @Override
    public void deleteAllPatientBed(Long roomId) {

        Optional<PatientRoom> patientRoom = patientRoomRepository.findById(roomId);

        if (patientRoom.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_ROOM_NOT_EXISTED);
        }

        List<PatientBed> patientBedsInRoom = patientRoom.get().getBeds();

        if (patientBedsInRoom.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_BED_IN_ROOM_NOT_EXISTED);
        }

        for (PatientBed patientBed: patientBedsInRoom) {
            if (!ObjectUtils.isEmpty(patientBed.getPatient())) {
                throw new PMSException(EnumPMSException.DELETE_PATIENT_BED_FAIL);
            }
        }

        patientBedRepository.deleteAll(patientBedsInRoom);
    }

    private void validateAndCorrectData (PatientBedRequest request) {
        if (ObjectUtils.isEmpty(request)
                || ObjectUtils.isEmpty(request.getStatus())
                || ObjectUtils.isEmpty(request.getRoomId())
                || ObjectUtils.isEmpty(request.getBedNumber())) {
            throw new PMSException(EnumPMSException.PATIENT_BED_INVALID_DATA);
        }

        request.setBedNumber(StringUtil.removeWhitespace(request.getBedNumber()));
    }

    private void checkDuplicatePatientBed(PatientBedRequest request, Long id) {
        PatientBed patientBedExisted = customPatientBedRepository.findPatientBedExisted(request);
        if (!ObjectUtils.isEmpty(patientBedExisted) && (ObjectUtils.isEmpty(id) || !patientBedExisted.getId().equals(id))) {
            throw new PMSException(EnumPMSException.PATIENT_BED_EXISTED);
        }
    }
}
