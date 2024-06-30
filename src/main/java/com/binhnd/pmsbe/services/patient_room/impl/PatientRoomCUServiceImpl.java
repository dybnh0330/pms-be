package com.binhnd.pmsbe.services.patient_room.impl;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.common.utils.jwt.SecurityUtils;
import com.binhnd.pmsbe.dto.request.*;
import com.binhnd.pmsbe.dto.response.PatientRoomResponse;
import com.binhnd.pmsbe.entity.Department;
import com.binhnd.pmsbe.entity.PatientBed;
import com.binhnd.pmsbe.entity.PatientRoom;
import com.binhnd.pmsbe.mapper.PatientRoomMapper;
import com.binhnd.pmsbe.repositories.DepartmentRepository;
import com.binhnd.pmsbe.repositories.PatientBedRepository;
import com.binhnd.pmsbe.repositories.PatientRoomRepository;
import com.binhnd.pmsbe.repositories.custom_repositories.CustomPatientRoomRepository;
import com.binhnd.pmsbe.services.patient_bed.PatientBedService;
import com.binhnd.pmsbe.services.patient_room.PatientRoomCUService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientRoomCUServiceImpl implements PatientRoomCUService {

    private final PatientRoomRepository patientRoomRepository;
    private final PatientBedRepository patientBedRepository;
    private final DepartmentRepository departmentRepository;
    private final CustomPatientRoomRepository customPatientRoomRepository;
    private final PatientBedService patientBedService;

    @Autowired
    public PatientRoomCUServiceImpl(PatientRoomRepository patientRoomRepository,
                                    PatientBedRepository patientBedRepository,
                                    DepartmentRepository departmentRepository,
                                    CustomPatientRoomRepository customPatientRoomRepository,
                                    PatientBedService patientBedService) {
        this.patientRoomRepository = patientRoomRepository;
        this.patientBedRepository = patientBedRepository;
        this.departmentRepository = departmentRepository;
        this.customPatientRoomRepository = customPatientRoomRepository;
        this.patientBedService = patientBedService;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public PatientRoomResponse createPatientRoom(PatientRoomHasBedRequest request) {

        validateAndCorrectData(request);

        checkDuplicatePatientRoom(request);

        Optional<Department> department = departmentRepository.findById(request.getDepartmentId());

        if (department.isEmpty()) {
            throw new PMSException(EnumPMSException.DEPARTMENT_NOT_EXIST);
        }

        UUID roomCode = UUID.randomUUID();

        PatientRoom patientRoom = new PatientRoom();
        patientRoom.setRoomCode(roomCode.toString());
        patientRoom.setRoomNumber(request.getRoomNumber());
        patientRoom.setStatus(request.getStatus());
        patientRoom.setTotalBed(request.getTotalBed());
        patientRoom.setDepartment(department.get());
        patientRoom.setCreateTime(Timestamp.from(Instant.now()));
        patientRoom.setCreateBy(SecurityUtils.getCurrentUsername());

        PatientRoom room = patientRoomRepository.save(patientRoom);

        List<BedNumber> bedNumbers = request.getPatientBeds();

        List<PatientBed> patientBeds = new ArrayList<>();

        for (BedNumber bedNumber : bedNumbers) {

            PatientBed patientBed = new PatientBed();

            UUID bedCode = UUID.randomUUID();
            patientBed.setBedCode(bedCode.toString());
            patientBed.setBedNumber(bedNumber.getBedNumber());
            patientBed.setPatientRoom(room);
            patientBed.setCreateTime(Timestamp.from(Instant.now()));
            patientBed.setCreateBy(SecurityUtils.getCurrentUsername());
            patientBed.setStatus(request.getStatus());

            Boolean checked = patientBedService.checkDuplicatePatientBed(patientBed, null);

            if (checked) {
                throw new PMSException(EnumPMSException.PATIENT_BED_EXISTED);
            }

            patientBedRepository.save(patientBed);

            patientBeds.add(patientBed);
        }

        patientRoom.setBeds(patientBeds);

        return PatientRoomMapper.toDto(room);
    }

    @Override
    public PatientRoomResponse updatePatientRoom(PatientRoomRequest request, Long id) {

        validateAndCorrectData(request);

        checkDuplicatePatientRoom(request, id);

        Optional<Department> department = departmentRepository.findById(request.getDepartmentId());
        if (department.isEmpty()) {
            throw new PMSException(EnumPMSException.DEPARTMENT_NOT_EXIST);
        }

        Optional<PatientRoom> patientRoom = patientRoomRepository.findById(id);
        if (patientRoom.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_ROOM_NOT_EXISTED);
        }

        PatientRoom patientRoomRequest = patientRoom.get();
        patientRoomRequest.setRoomNumber(request.getRoomNumber());
        patientRoomRequest.setTotalBed(request.getTotalBed());
        //trường hợp ban đầu khi ấn sửa, status = true
        patientRoomRequest.setStatus(request.getStatus());


        //trường hợp khi tự chọn status = false và giường bệnh không trống
        if (Boolean.FALSE.equals(request.getStatus()) && !patientRoomRequest.getBeds().isEmpty()) {
            for (PatientBed bed : patientRoomRequest.getBeds()) {
                bed.setStatus(false);
                patientBedRepository.save(bed);
            }
        }

        //số giường cập nhật phải >= số giường hiện tại đang có trong buồng bệnh
        if (patientRoomRequest.getBeds().size() > request.getTotalBed()) {
            throw new PMSException(EnumPMSException.UPDATE_PATIENT_ROOM_FAIL);
        }

        patientRoomRequest.setDepartment(department.get());
        patientRoomRequest.setUpdateTime(Timestamp.from(Instant.now()));
        patientRoomRequest.setUpdateBy(SecurityUtils.getCurrentUsername());

        patientRoomRepository.save(patientRoomRequest);

        return PatientRoomMapper.toDto(patientRoomRequest);
    }

    @Override
    public void deletePatientRoom(Long id) {
        Optional<PatientRoom> patientRoom = patientRoomRepository.findById(id);

        if (patientRoom.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_ROOM_NOT_EXISTED);
        }

        //có giường bệnh => kh thể delete buồng
        if (!patientRoom.get().getBeds().isEmpty()) {
            throw new PMSException(EnumPMSException.DELETE_PATIENT_ROOM_FAIL);
        } else {
            patientRoomRepository.deleteById(id);
        }
    }

    private void validateAndCorrectData(PatientRoomHasBedRequest request) {
        if (!ObjectUtils.isEmpty(request.getPatientBeds())) {
            if (ObjectUtils.isEmpty(request)
                    || ObjectUtils.isEmpty(request.getStatus())
                    || ObjectUtils.isEmpty(request.getTotalBed())
                    || request.getTotalBed() < request.getPatientBeds().size()
                    || ObjectUtils.isEmpty(request.getRoomNumber())) {
                throw new PMSException(EnumPMSException.PATIENT_ROOM_DATA_INVALID);
            }
        }

        request.setRoomNumber(StringUtil.removeWhitespace(request.getRoomNumber()));
    }

    private void validateAndCorrectData(PatientRoomRequest request) {
        if (ObjectUtils.isEmpty(request)
                || ObjectUtils.isEmpty(request.getStatus())
                || ObjectUtils.isEmpty(request.getTotalBed())
                || ObjectUtils.isEmpty(request.getRoomNumber())) {
            throw new PMSException(EnumPMSException.PATIENT_ROOM_DATA_INVALID);
        }

        request.setRoomNumber(StringUtil.removeWhitespace(request.getRoomNumber()));
    }

    private void checkDuplicatePatientRoom(PatientRoomRequest request, Long id) {
        PatientRoom patientRoomExisted = customPatientRoomRepository.findPatientRoomExisted(request);
        if (!ObjectUtils.isEmpty(patientRoomExisted) && (ObjectUtils.isEmpty(id) || !patientRoomExisted.getId().equals(id))) {
            throw new PMSException(EnumPMSException.PATIENT_ROOM_EXISTED);
        }
    }

    private void checkDuplicatePatientRoom(PatientRoomHasBedRequest request) {
        PatientRoom patientRoomExisted = customPatientRoomRepository.findPatientRoomExisted(request);
        if (!ObjectUtils.isEmpty(patientRoomExisted)) {
            throw new PMSException(EnumPMSException.PATIENT_ROOM_EXISTED);
        }
    }
}
