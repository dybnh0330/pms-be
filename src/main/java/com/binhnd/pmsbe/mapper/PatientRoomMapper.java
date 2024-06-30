package com.binhnd.pmsbe.mapper;

import com.binhnd.pmsbe.dto.response.PatientRoomResponse;
import com.binhnd.pmsbe.entity.Department;
import com.binhnd.pmsbe.entity.PatientBed;
import com.binhnd.pmsbe.entity.PatientRoom;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class PatientRoomMapper {

    public static PatientRoomResponse toDto(PatientRoom entity) {
        PatientRoomResponse dto = new PatientRoomResponse();
        dto.setId(entity.getId());
        dto.setRoomCode(entity.getRoomCode());
        dto.setRoomNumber(entity.getRoomNumber());
        dto.setStatus(entity.getStatus());
        dto.setTotalBed(entity.getTotalBed());

        Department department = entity.getDepartment();
        if (!ObjectUtils.isEmpty(department)) {
            dto.setDepartmentId(department.getId());
            dto.setDepartmentName(department.getName());
        }

        List<Long> bedIds = new ArrayList<>();
        List<String> bedNumbers = new ArrayList<>();

        if (!ObjectUtils.isEmpty(entity.getBeds()) && !entity.getBeds().isEmpty()) {
            for (PatientBed bed : entity.getBeds()) {
                bedIds.add(bed.getId());
                bedNumbers.add(bed.getBedNumber());
            }
            dto.setBedIds(bedIds);
            dto.setBedNumbers(bedNumbers);
        }

        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        dto.setCreateBy(entity.getCreateBy());
        dto.setUpdateBy(entity.getUpdateBy());
        return dto;
    }

    public static List<PatientRoomResponse> mapAll(List<PatientRoom> patientRooms) {
        List<PatientRoomResponse> responses = new ArrayList<>();
        for (PatientRoom patientRoom : patientRooms) {
            responses.add(toDto(patientRoom));
        }
        return responses;
    }

}
