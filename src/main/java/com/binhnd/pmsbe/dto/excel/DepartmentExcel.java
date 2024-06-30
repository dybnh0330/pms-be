package com.binhnd.pmsbe.dto.excel;

import com.binhnd.pmsbe.common.constants.PMSExcelConstant;
import com.binhnd.pmsbe.common.utils.DateUtil;
import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.entity.Department;
import lombok.Data;
import lombok.Generated;

@Data
@Generated
public class DepartmentExcel {
    private Integer order;
    private String name;
    private String description;
    private String createTime;
    private String createBy;
    private String updateTime;
    private String updateBy;
    private String error;

    public DepartmentExcel() {
    }

    public static DepartmentExcel convert(Department department) {
        DepartmentExcel departmentExcel = new DepartmentExcel();
        departmentExcel.name = department.getName();
        departmentExcel.description = department.getDescription();
        departmentExcel.createTime = department.getCreateTime() == null
                ? StringUtil.EMPTY
                : DateUtil.converterToString(department.getCreateTime(), PMSExcelConstant.FORMAT_DAY_HOUR);
        departmentExcel.createBy = department.getCreateBy();
        departmentExcel.updateTime = department.getUpdateTime() == null
                ? StringUtil.EMPTY
                : DateUtil.converterToString(department.getCreateTime(), PMSExcelConstant.FORMAT_DAY_HOUR);
        departmentExcel.updateBy = department.getUpdateBy();
        return departmentExcel;
    }
}
