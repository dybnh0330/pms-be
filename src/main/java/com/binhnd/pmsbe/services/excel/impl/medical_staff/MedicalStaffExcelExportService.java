package com.binhnd.pmsbe.services.excel.impl.medical_staff;

import com.binhnd.pmsbe.common.constants.PMSExcelConstant;
import com.binhnd.pmsbe.common.enums.EnumAlignExcel;
import com.binhnd.pmsbe.dto.excel.MedicalStaffExcel;
import com.binhnd.pmsbe.entity.Department;
import com.binhnd.pmsbe.entity.HeaderColumnExcel;
import com.binhnd.pmsbe.entity.MedicalStaff;
import com.binhnd.pmsbe.repositories.DepartmentRepository;
import com.binhnd.pmsbe.repositories.MedicalStaffRepository;
import com.binhnd.pmsbe.services.excel.AbstractWriteExcelService;
import com.binhnd.pmsbe.services.excel.ExportExcelService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service("medicalStaffExcelExportService")
public class MedicalStaffExcelExportService extends AbstractWriteExcelService implements ExportExcelService {

    private final MedicalStaffRepository medicalStaffRepository;

    private final DepartmentRepository departmentRepository;
    @Autowired
    public MedicalStaffExcelExportService(MedicalStaffRepository medicalStaffRepository,
                                          DepartmentRepository departmentRepository) {
        this.medicalStaffRepository = medicalStaffRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Workbook downloadExcel() {

        List<MedicalStaff> medicalStaffs = medicalStaffRepository.findAllByDepartmentIsNotNull();

        Map<Long, String> departmentMap = departmentRepository.findAll().stream()
                .collect(Collectors.toMap(Department::getId, Department::getName));

        AtomicInteger order = new AtomicInteger(1);
        List<MedicalStaffExcel> medicalStaffExcels = medicalStaffs.stream()
                .map(medicalStaff -> MedicalStaffExcel.convert(medicalStaff, departmentMap))
                .peek(medicalStaffExcel -> medicalStaffExcel.setOrder(order.getAndIncrement()))
                .collect(Collectors.toList());

        setHeaderMap(getHeaderExcelMap());

        createWriter();
        appendData(medicalStaffExcels);

        return write();
    }

    @Override
    public String getSheetName() {
        return PMSExcelConstant.MEDICAL_STAFF_SHEET_NAME;
    }

    @Override
    public String getTitle() {
        return PMSExcelConstant.STAFF_CONTAIN_HEADER;
    }

    @Override
    public short getHeaderHeight() {
        return 600;
    }

    @Override
    public short getTitleHeight() {
        return 700;
    }

    @Override
    public void drawFooter() {

    }

    private Map<String, HeaderColumnExcel> getHeaderExcelMap() {
        Map<String, HeaderColumnExcel> headerList = new LinkedHashMap<>();
        headerList.put(PMSExcelConstant.BaseExcelColumn.ORDER_COLUMN.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.BaseExcelColumn.ORDER_COLUMN.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        headerList.put(PMSExcelConstant.MedicalStaffColumn.NAME_COLUMN.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.MedicalStaffColumn.NAME_COLUMN.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        headerList.put(PMSExcelConstant.MedicalStaffColumn.GENDER_COLUMN.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.MedicalStaffColumn.GENDER_COLUMN.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        headerList.put(PMSExcelConstant.MedicalStaffColumn.DOB_COLUMN.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.MedicalStaffColumn.DOB_COLUMN.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        headerList.put(PMSExcelConstant.MedicalStaffColumn.ADDRESS_COLUMN.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.MedicalStaffColumn.ADDRESS_COLUMN.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        headerList.put(PMSExcelConstant.MedicalStaffColumn.CCCD_COLUMN.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.MedicalStaffColumn.CCCD_COLUMN.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        headerList.put(PMSExcelConstant.MedicalStaffColumn.PHONE_NUMBER_COLUMN.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.MedicalStaffColumn.PHONE_NUMBER_COLUMN.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        headerList.put(PMSExcelConstant.MedicalStaffColumn.EMAIL_COLUMN.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.MedicalStaffColumn.EMAIL_COLUMN.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        headerList.put(PMSExcelConstant.MedicalStaffColumn.CERTIFICATE_COLUMN.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.MedicalStaffColumn.CERTIFICATE_COLUMN.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        headerList.put(PMSExcelConstant.MedicalStaffColumn.SPECIALIZE_COLUMN.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.MedicalStaffColumn.SPECIALIZE_COLUMN.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        headerList.put(PMSExcelConstant.MedicalStaffColumn.DEPARTMENT_COLUMN.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.MedicalStaffColumn.DEPARTMENT_COLUMN.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        headerList.put(PMSExcelConstant.BaseExcelColumn.CREATE_TIME.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.BaseExcelColumn.CREATE_TIME.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        headerList.put(PMSExcelConstant.BaseExcelColumn.CREATE_BY.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.BaseExcelColumn.CREATE_BY.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        headerList.put(PMSExcelConstant.BaseExcelColumn.UPDATE_TIME.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.BaseExcelColumn.UPDATE_TIME.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        headerList.put(PMSExcelConstant.BaseExcelColumn.UPDATE_BY.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.BaseExcelColumn.UPDATE_BY.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());

        return headerList;
    }
}
