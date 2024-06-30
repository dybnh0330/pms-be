package com.binhnd.pmsbe.dto.excel;

import com.binhnd.pmsbe.common.constants.PMSExcelConstant;
import com.binhnd.pmsbe.common.utils.DateUtil;
import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.entity.MedicalStaff;
import lombok.Data;
import lombok.Generated;

import java.util.Map;

@Data
@Generated
public class MedicalStaffExcel {
    private Integer order;
    private String name;
    private String gender;
    private String dob;
    private String address;
    private String cccd;
    private String phoneNumber;
    private String email;
    private String certificate;
    private String specialize;
    private String department;
    private String createTime;
    private String createBy;
    private String updateTime;
    private String updateBy;

    public MedicalStaffExcel() {
    }

    public static MedicalStaffExcel convert(MedicalStaff medicalStaff, Map<Long, String> departmentMap) {
        MedicalStaffExcel medicalStaffExcel = new MedicalStaffExcel();
        medicalStaffExcel.name = medicalStaff.getName();
        medicalStaffExcel.gender = StringUtil.convertGenderToString(medicalStaff.getGender());
        medicalStaffExcel.dob = DateUtil.converterToString(medicalStaff.getDob(), PMSExcelConstant.FORMAT_DAY_HOUR_DOB);
        medicalStaffExcel.address = medicalStaff.getAddress();
        medicalStaffExcel.cccd = medicalStaff.getCccd();
        medicalStaffExcel.phoneNumber = medicalStaff.getPhoneNumber();
        medicalStaffExcel.email = medicalStaff.getEmail();
        medicalStaffExcel.certificate = medicalStaff.getCertificate();
        medicalStaffExcel.specialize = medicalStaff.getSpecialize();
        medicalStaffExcel.department = departmentMap.get(medicalStaff.getDepartment().getId());
        medicalStaffExcel.createTime = medicalStaff.getCreateTime() == null
                ? StringUtil.EMPTY
                : DateUtil.converterToString(medicalStaff.getCreateTime(), PMSExcelConstant.FORMAT_DAY_HOUR);
        medicalStaffExcel.createBy = medicalStaff.getCreateBy();
        medicalStaffExcel.updateTime = medicalStaff.getUpdateTime() == null
                ? StringUtil.EMPTY
                : DateUtil.converterToString(medicalStaff.getCreateTime(), PMSExcelConstant.FORMAT_DAY_HOUR);
        medicalStaffExcel.updateBy = medicalStaff.getUpdateBy();
        return medicalStaffExcel;
    }
}
