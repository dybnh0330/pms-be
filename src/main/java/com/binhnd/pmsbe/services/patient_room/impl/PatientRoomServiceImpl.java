package com.binhnd.pmsbe.services.patient_room.impl;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.PatientRoomResponse;
import com.binhnd.pmsbe.entity.PatientRoom;
import com.binhnd.pmsbe.mapper.PatientRoomMapper;
import com.binhnd.pmsbe.repositories.PatientRoomRepository;
import com.binhnd.pmsbe.repositories.custom_repositories.CustomPatientRoomRepository;
import com.binhnd.pmsbe.services.patient_room.PatientRoomService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientRoomServiceImpl implements PatientRoomService {

    private final PatientRoomRepository patientRoomRepository;
    private final CustomPatientRoomRepository customPatientRoomRepository;

    public PatientRoomServiceImpl(PatientRoomRepository patientRoomRepository,
                                  CustomPatientRoomRepository customPatientRoomRepository) {
        this.patientRoomRepository = patientRoomRepository;
        this.customPatientRoomRepository = customPatientRoomRepository;
    }

    @Override
    public List<PatientRoomResponse> findAllPatientRoomByDepartmentId(Long departmentId) {
        return PatientRoomMapper.mapAll(customPatientRoomRepository.findAllPatientRoomByDepartment(departmentId));
    }

    @Override
    public List<PatientRoomResponse> findAllPatientRoomEmptyByDepartmentId(Long departmentId) {
        return PatientRoomMapper.mapAll(customPatientRoomRepository.findAllPatientRoomEmptyByDepartment(departmentId));
    }

    @Override
    public Page<PatientRoomResponse> findAllPatientRoomByDepartmentPage(Long departmentId, SearchSortPageableDTO dto) {
        return customPatientRoomRepository.findAllPatientRoomByDeparmentPage(departmentId, dto);
    }

    @Override
    public PatientRoomResponse findPatientRoomById(Long id) {
        Optional<PatientRoom> patientRoom = patientRoomRepository.findById(id);

        if (patientRoom.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_ROOM_NOT_EXISTED);
        }

        return PatientRoomMapper.toDto(patientRoom.get());
    }
}
