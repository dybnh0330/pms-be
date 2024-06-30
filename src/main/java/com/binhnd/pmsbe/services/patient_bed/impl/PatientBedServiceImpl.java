package com.binhnd.pmsbe.services.patient_bed.impl;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.dto.response.PatientBedResponse;
import com.binhnd.pmsbe.entity.PatientBed;
import com.binhnd.pmsbe.entity.PatientRoom;
import com.binhnd.pmsbe.mapper.PatientBedMapper;
import com.binhnd.pmsbe.repositories.PatientBedRepository;
import com.binhnd.pmsbe.repositories.PatientRoomRepository;
import com.binhnd.pmsbe.repositories.custom_repositories.CustomPatientBedRepository;
import com.binhnd.pmsbe.services.patient_bed.PatientBedService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientBedServiceImpl implements PatientBedService {

    private final PatientBedRepository patientBedRepository;

    private final PatientRoomRepository patientRoomRepository;

    private final CustomPatientBedRepository customPatientBedRepository;

    @Autowired
    public PatientBedServiceImpl(PatientBedRepository patientBedRepository,
                                 PatientRoomRepository patientRoomRepository,
                                 CustomPatientBedRepository customPatientBedRepository) {
        this.patientBedRepository = patientBedRepository;
        this.patientRoomRepository = patientRoomRepository;
        this.customPatientBedRepository = customPatientBedRepository;
    }

    @Override
    public List<PatientBedResponse> findAllBedByRoomId(Long roomId) {
        return PatientBedMapper.mapAll(customPatientBedRepository.findAllPatientBedByRoomId(roomId));
    }

    @Override
    public List<PatientBedResponse> findAllBedEmptyByRoom(Long roomId) {
        return PatientBedMapper.mapAll(customPatientBedRepository.findAllBedEmptyByRoom(roomId));
    }

    @Override
    public PatientBedResponse findPatientBedById(Long id) {

        Optional<PatientBed> patientBed = patientBedRepository.findById(id);
        if (patientBed.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_BED_NOT_EXISTED);
        }

        PatientBed response = patientBed.get();

        return PatientBedMapper.toDto(response);
    }

    @Override
    public Boolean checkDuplicatePatientBed(PatientBed request, Long id) {

        Optional<PatientRoom> optionalPatientRoom = patientRoomRepository
                .findById(request.getPatientRoom().getId());

        if (optionalPatientRoom.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_ROOM_NOT_EXISTED);
        }

        PatientRoom patientRoom = optionalPatientRoom.get();

        PatientBed patientBedExisted = patientBedRepository.findPatientBedByBedNumberAndPatientRoom(request.getBedNumber(), patientRoom);


        //1) nếu patientBedExisted != null => true && true => true (patient existed)
        //2) nếu patientBedExisted == null = > false && true => false (patient not existed)
        //3) patientBedExisted.getId() == null => false (patient not existed)
        //4) patientBedExisted.getId() != null => true (patient existed)
        return !ObjectUtils.isEmpty(patientBedExisted) && (ObjectUtils.isEmpty(id) || !patientBedExisted.getId().equals(id));

    }
}
