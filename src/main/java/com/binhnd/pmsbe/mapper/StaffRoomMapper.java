package com.binhnd.pmsbe.mapper;

import com.binhnd.pmsbe.dto.response.MedicalStaffResponse;
import com.binhnd.pmsbe.dto.response.StaffRoomResponse;
import com.binhnd.pmsbe.dto.response.UpdateStaffInRoom;
import com.binhnd.pmsbe.entity.MedicalStaff;
import com.binhnd.pmsbe.entity.PatientRoom;
import com.binhnd.pmsbe.entity.StaffRoom;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class StaffRoomMapper {

    public static StaffRoomResponse toDto (List<StaffRoom> entities) {
        StaffRoomResponse dto = new StaffRoomResponse();

        List<MedicalStaffResponse> responses = new ArrayList<>();

        for (StaffRoom entity: entities) {
            PatientRoom room = entity.getRoom();
            if (!ObjectUtils.isEmpty(room)) {
                dto.setRoomId(room.getId());
                dto.setRoomNumber(room.getRoomNumber());
            }

            MedicalStaff medicalStaff = entity.getNurse();

            MedicalStaffResponse response = MedicalStaffMapper.toDto(medicalStaff);

            responses.add(response);

            dto.setCreateTime(entity.getCreateTime());
            dto.setUpdateTime(entity.getUpdateTime());
            dto.setCreateBy(entity.getCreateBy());
            dto.setUpdateBy(entity.getUpdateBy());
        }

        dto.setStaffs(responses);

        return dto;
    }

    public static UpdateStaffInRoom toStaffRoomUpdate (List<StaffRoom> entities) {
        UpdateStaffInRoom dto = new UpdateStaffInRoom();

        List<Long> medicalStaffIds = new ArrayList<>();
        List<String> medicalStaffNames = new ArrayList<>();

        for (StaffRoom entity : entities) {
            dto.setDepartmentId(entity.getRoom().getDepartment().getId());
            dto.setRoomId(entity.getRoom().getId());
            dto.setRoomNumber(entity.getRoom().getRoomNumber());

            MedicalStaff nurse = entity.getNurse();
            if (!ObjectUtils.isEmpty(nurse)) {
                medicalStaffNames.add(nurse.getName());
                medicalStaffIds.add(nurse.getId());
            }
        }
        dto.setMedicalStaffIds(medicalStaffIds);
        dto.setMedicalStaffNames(medicalStaffNames);

        return dto;
    }

}
