package com.binhnd.pmsbe.mapper;

import com.binhnd.pmsbe.dto.response.MedicalStaffResponse;
import com.binhnd.pmsbe.entity.Department;
import com.binhnd.pmsbe.entity.MedicalStaff;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class MedicalStaffMapper {

    private MedicalStaffMapper() {
    }

    public static MedicalStaffResponse toDto(MedicalStaff entity) {
        MedicalStaffResponse dto = new MedicalStaffResponse();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAddress(entity.getAddress());
        dto.setGender(entity.getGender());
        dto.setCccd(entity.getCccd());
        dto.setEmail(entity.getEmail());
        dto.setCertificate(entity.getCertificate());
        dto.setSpecialize(entity.getSpecialize());
        dto.setDob(entity.getDob());
        dto.setPhoneNumber(entity.getPhoneNumber());

        if (!ObjectUtils.isEmpty(entity.getAccount())) {
            dto.setUsername(entity.getAccount().getUsername());
        }

        Department department = entity.getDepartment();
        if (!ObjectUtils.isEmpty(department)) {
            dto.setDepartmentId(department.getId());
            dto.setDepartmentName(department.getName());
        }

        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateBy(entity.getUpdateBy());
        dto.setCreateBy(entity.getCreateBy());
        dto.setUpdateTime(entity.getUpdateTime());

        return dto;
    }

    public static List<MedicalStaffResponse> mapAll(List<MedicalStaff> medicalStaffs) {
        List<MedicalStaffResponse> responses = new ArrayList<>();
        for (MedicalStaff medicalStaff : medicalStaffs) {
            responses.add(toDto(medicalStaff));
        }
        return responses;
    }
}
