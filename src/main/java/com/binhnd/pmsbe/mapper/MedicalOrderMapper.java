package com.binhnd.pmsbe.mapper;

import com.binhnd.pmsbe.dto.response.MedicalOrderDetailsResponse;
import com.binhnd.pmsbe.dto.response.MedicalOrderResponse;
import com.binhnd.pmsbe.entity.MedicalOrder;
import com.binhnd.pmsbe.entity.MedicalOrderDetails;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MedicalOrderMapper {

    private MedicalOrderMapper() {}

    public static MedicalOrderDetailsResponse toOrderDetailsDto (MedicalOrderDetails entity) {
        MedicalOrderDetailsResponse dto = new MedicalOrderDetailsResponse();

        dto.setId(entity.getId());
        dto.setOrderName(entity.getCategory().getName());
        dto.setNote(entity.getNote());
        dto.setUnit(entity.getUnit());

        if (entity.getType().equals(0L)) {
            dto.setType("Thuốc điều trị");
        } else if (entity.getType().equals(1L)) {
            dto.setType("Xét nghiệm CLS");
        } else {
            dto.setType("Phẫu thuật/Thủ thuật");
        }

        dto.setQuantity(entity.getQuantity());
        dto.setCreateTime(entity.getCreateTime());
        dto.setCreateBy(entity.getCreateBy());

        return dto;
    }

    public static List<MedicalOrderDetailsResponse> mapAll(List<MedicalOrderDetails> entities) {
        List<MedicalOrderDetailsResponse> medicalOrderDetailsRespons = new ArrayList<>();
        for (MedicalOrderDetails details: entities) {
            medicalOrderDetailsRespons.add(toOrderDetailsDto(details));
        }
        return medicalOrderDetailsRespons;
    }

    public static MedicalOrderResponse toOrderDto(MedicalOrder medicalOrder) {
        MedicalOrderResponse dto = new MedicalOrderResponse();
        dto.setId(medicalOrder.getId());
        dto.setTitle(medicalOrder.getTitle());
        dto.setCreateTime(Timestamp.from(Instant.now()));
        dto.setPatientId(medicalOrder.getPatient().getId());
        dto.setPatientName(medicalOrder.getPatient().getName());

        return dto;
    }
}
