package com.binhnd.pmsbe.mapper;

import com.binhnd.pmsbe.dto.response.PatientBedResponse;
import com.binhnd.pmsbe.entity.PatientBed;
import com.binhnd.pmsbe.entity.PatientRoom;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class PatientBedMapper {

    public static PatientBedResponse toDto (PatientBed entity) {
        PatientBedResponse dto = new PatientBedResponse();

        dto.setId(entity.getId());
        dto.setBedCode(entity.getBedCode());
        dto.setBedNumber(entity.getBedNumber());
        dto.setStatus(entity.getStatus());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        dto.setCreateBy(entity.getCreateBy());
        dto.setUpdateBy(entity.getUpdateBy());

        PatientRoom patientRoom = entity.getPatientRoom();
        if (!ObjectUtils.isEmpty(patientRoom)) {
            dto.setRoomNumber(patientRoom.getRoomNumber());
            dto.setRoomId(patientRoom.getId());
        }

        return dto;
    }

    public static List<PatientBedResponse> mapAll(List<PatientBed> patientBeds) {
        List<PatientBedResponse> responses = new ArrayList<>();
        for (PatientBed patientBed : patientBeds) {
            responses.add(toDto(patientBed));
        }
        return responses;
    }


}
