package com.binhnd.pmsbe.mapper;

import com.binhnd.pmsbe.dto.response.MedicalRecordDetailResponse;
import com.binhnd.pmsbe.dto.response.MedicalRecordResponse;
import com.binhnd.pmsbe.dto.response.ResultResponse;
import com.binhnd.pmsbe.entity.MedicalRecord;
import com.binhnd.pmsbe.entity.MedicalRecordDetails;
import com.binhnd.pmsbe.entity.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MedicalRecordMapper {

    private MedicalRecordMapper() {
    }

    public static MedicalRecordResponse toDto(MedicalRecord entity) {

        MedicalRecordResponse dto = new MedicalRecordResponse();

        dto.setRecordId(entity.getId());
        dto.setRecordCode(entity.getRecordCode());
        dto.setPatientId(entity.getPatient().getId());
        dto.setPatientName(entity.getPatient().getName());
        dto.setReason(entity.getReason());
        dto.setMedicalHistory(entity.getMedicalHistory());
        dto.setDiagnostic(entity.getDiagnostic());

        return dto;
    }

    public static MedicalRecordDetailResponse toDto(MedicalRecordDetails entity) {

        MedicalRecordDetailResponse dto = new MedicalRecordDetailResponse();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        dto.setCreateBy(entity.getCreateBy());
        dto.setUpdateBy(entity.getUpdateBy());

        return dto;
    }

    public static List<MedicalRecordDetailResponse> mapAll(List<MedicalRecordDetails> entities) {

        List<MedicalRecordDetailResponse> responses = new ArrayList<>();
        for (MedicalRecordDetails entity : entities) {
            responses.add(toDto(entity));
        }

        return responses;
    }
}
