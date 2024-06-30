package com.binhnd.pmsbe.services.staff_room.impl;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.dto.request.StaffRoomRequest;
import com.binhnd.pmsbe.dto.response.StaffRoomResponse;
import com.binhnd.pmsbe.dto.response.UpdateStaffInRoom;
import com.binhnd.pmsbe.entity.MedicalStaff;
import com.binhnd.pmsbe.entity.PatientRoom;
import com.binhnd.pmsbe.entity.StaffRoom;
import com.binhnd.pmsbe.mapper.StaffRoomMapper;
import com.binhnd.pmsbe.repositories.MedicalStaffRepository;
import com.binhnd.pmsbe.repositories.PatientRoomRepository;
import com.binhnd.pmsbe.repositories.StaffRoomRepository;
import com.binhnd.pmsbe.repositories.custom_repositories.CustomStaffRoomRepository;
import com.binhnd.pmsbe.services.staff_room.StaffRoomService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StaffRoomServiceImpl implements StaffRoomService {

    private final PatientRoomRepository patientRoomRepository;
    private final MedicalStaffRepository medicalStaffRepository;
    private final StaffRoomRepository staffRoomRepository;
    private final CustomStaffRoomRepository customStaffRoomRepository;

    public StaffRoomServiceImpl(PatientRoomRepository patientRoomRepository,
                                MedicalStaffRepository medicalStaffRepository,
                                StaffRoomRepository staffRoomRepository,
                                CustomStaffRoomRepository customStaffRoomRepository) {
        this.patientRoomRepository = patientRoomRepository;
        this.medicalStaffRepository = medicalStaffRepository;
        this.staffRoomRepository = staffRoomRepository;
        this.customStaffRoomRepository = customStaffRoomRepository;
    }

    @Override
    public void updateStaffInRoom(Long id, StaffRoomRequest request) {

        List<MedicalStaff> medicalStaffs = new ArrayList<>();

        Optional<PatientRoom> patientRoomRequest = patientRoomRepository.findById(id);

        if (patientRoomRequest.isEmpty()) {
            throw new PMSException(EnumPMSException.PATIENT_ROOM_NOT_EXISTED);
        }

        PatientRoom patientRoom = patientRoomRequest.get();

        List<MedicalStaff> findMedicalStaffByDepartmentId = patientRoom.getDepartment().getMedicalStaffs();

        for (Long staffId: request.getMedicalStaffIds()) {
            Optional<MedicalStaff> medicalStaff = medicalStaffRepository.findById(staffId);

            if (medicalStaff.isEmpty()) {
                throw new PMSException(EnumPMSException.MEDICAL_STAFF_NOT_EXISTED);
            }

            if (!findMedicalStaffByDepartmentId.contains(medicalStaff.get())) {
                throw new PMSException(EnumPMSException.STAFF_NOT_IN_DEPARTMENT);
            }

            medicalStaffs.add(medicalStaff.get());
        }

        List<StaffRoom> staffRooms = customStaffRoomRepository.findAllStaffInRoom(id);

        if (!staffRooms.isEmpty()) {
            staffRoomRepository.deleteAll(staffRooms);
        }

        for (MedicalStaff staff: medicalStaffs) {
            StaffRoom staffRoom = new StaffRoom();
            staffRoom.setRoom(patientRoom);
            staffRoom.setCreateTime(Timestamp.from(Instant.now()));
            staffRoom.setUpdateTime(Timestamp.from(Instant.now()));
            staffRoom.setUpdateBy("admin");
            staffRoom.setCreateBy("admin");
            staffRoom.setNurse(staff);
            staffRoomRepository.save(staffRoom);
        }
    }

    @Override
    public StaffRoomResponse showStaffInRoom(Long roomId) {

        List<StaffRoom> allStaffInRoom = customStaffRoomRepository.findAllStaffInRoom(roomId);

        return StaffRoomMapper.toDto(allStaffInRoom);
    }

    @Override
    public UpdateStaffInRoom findAllStaffInRoom(Long roomId) {

        List<StaffRoom> staffRooms = customStaffRoomRepository.findAllStaffInRoom(roomId);

        return StaffRoomMapper.toStaffRoomUpdate(staffRooms);
    }
}
