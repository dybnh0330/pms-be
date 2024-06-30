package com.binhnd.pmsbe.services.excel.impl.department;

import com.binhnd.pmsbe.common.constants.PMSExcelConstant;
import com.binhnd.pmsbe.common.enums.EnumAlignExcel;
import com.binhnd.pmsbe.dto.excel.DepartmentExcel;
import com.binhnd.pmsbe.entity.Department;
import com.binhnd.pmsbe.entity.HeaderColumnExcel;
import com.binhnd.pmsbe.repositories.DepartmentRepository;
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

@Service("departmentExportExcelService")
public class DepartmentExcelExportService extends AbstractWriteExcelService implements ExportExcelService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentExcelExportService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Workbook downloadExcel() {

        List<Department> departments = departmentRepository.findAll();

        AtomicInteger order = new AtomicInteger(1);
        List<DepartmentExcel> departmentExcels = departments.stream()
                .map(DepartmentExcel::convert)
                .peek(departmentExcel -> departmentExcel.setOrder(order.getAndIncrement()))
                .collect(Collectors.toList());

        setHeaderMap(getHeaderExcelMap());
        createWriter();
        appendData(departmentExcels);

        return write();
    }

    @Override
    public String getSheetName() {
        return PMSExcelConstant.DEPARTMENT_SHEET_NAME;
    }

    @Override
    public String getTitle() {
        return PMSExcelConstant.DEPARTMENT_CONTAIN_HEADER;
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
        headerList.put(PMSExcelConstant.DepartmentColumn.DEPARTMENT_NAME_COLUMN.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.DepartmentColumn.DEPARTMENT_NAME_COLUMN.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        headerList.put(PMSExcelConstant.DepartmentColumn.DEPARTMENT_DESCRIPTION_COLUMN.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.DepartmentColumn.DEPARTMENT_DESCRIPTION_COLUMN.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        headerList.put(PMSExcelConstant.BaseExcelColumn.CREATE_BY.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.BaseExcelColumn.CREATE_BY.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        headerList.put(PMSExcelConstant.BaseExcelColumn.CREATE_TIME.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.BaseExcelColumn.CREATE_TIME.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        headerList.put(PMSExcelConstant.BaseExcelColumn.UPDATE_BY.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.BaseExcelColumn.UPDATE_BY.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        headerList.put(PMSExcelConstant.BaseExcelColumn.UPDATE_TIME.getField(),
                HeaderColumnExcel.builder()
                        .columnName(PMSExcelConstant.BaseExcelColumn.UPDATE_TIME.getHeader())
                        .align(EnumAlignExcel.CENTER)
                        .build());
        return headerList;
    }
}
