package com.binhnd.pmsbe.controller;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.common.constants.RequestAction;
import com.binhnd.pmsbe.common.utils.ExcelUtils;
import com.binhnd.pmsbe.dto.request.DepartmentRequest;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.DepartmentResponse;
import com.binhnd.pmsbe.services.department.DepartmentCUService;
import com.binhnd.pmsbe.services.department.DepartmentService;
import com.binhnd.pmsbe.services.excel.ExportExcelService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.binhnd.pmsbe.common.constants.RequestAction.DEPARTMENT;
import static com.binhnd.pmsbe.common.constants.RequestAction.Department.*;
import static com.binhnd.pmsbe.common.utils.ExcelUtils.sendExcelToClient;

@RestController
@RequestMapping(PMSConstants.PREFIX_URL + DEPARTMENT)
public class DepartmentController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final DepartmentCUService departmentCUService;

    private final DepartmentService departmentService;

    private final ExportExcelService departmentExportExcelService;

    @Autowired
    public DepartmentController(DepartmentCUService departmentCUService,
                                DepartmentService departmentService,
                                ExportExcelService departmentExportExcelService) {
        this.departmentCUService = departmentCUService;
        this.departmentService = departmentService;
        this.departmentExportExcelService = departmentExportExcelService;
    }

    @GetMapping(value = RequestAction.Department.FIND_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DepartmentResponse>> findAllDepartment() {
        LOGGER.info("[GET]{}/ get all department", PMSConstants.PREFIX_URL + "/department/find-all");
        List<DepartmentResponse> departments = departmentService.findAllDepartment();
        return ResponseEntity.ok().body(departments);
    }

    @GetMapping(value = FIND_ALL_PAGE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<DepartmentResponse>> findAllPageDepartment(SearchSortPageableDTO dto) {
        LOGGER.info("[GET]{}/ get all page department", PMSConstants.PREFIX_URL + "/department/page/find-all");
        Page<DepartmentResponse> departments = departmentService.findAllPageDepartment(dto);
        return ResponseEntity.ok().body(departments);
    }

    @GetMapping(value = FIND_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepartmentResponse> findDepartmentById(@RequestParam Long id) {
        LOGGER.info("[GET]{}/ get a department by id", PMSConstants.PREFIX_URL + "/department/find-by-id?id=" + id);
        DepartmentResponse department = departmentService.findDepartmentById(id);
        return ResponseEntity.ok(department);
    }

    @GetMapping(value = DOWNLOAD_EXCEL, produces = ExcelUtils.EXCEL_TYPE)
    public ResponseEntity<InputStreamResource> downloadDepartmentExcel(HttpServletResponse response) {
        LOGGER.info("[GET]/department/download-excel :: Download department list Excel file");
        Workbook workbook = departmentExportExcelService.downloadExcel();
        return sendExcelToClient(workbook, response, "department");
    }

    @PostMapping(value = CREATE_DEPARTMENT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepartmentResponse> createDepartment(@RequestBody DepartmentRequest request) {
        LOGGER.info("[POST]{}/ create a new department", PMSConstants.PREFIX_URL + "/department/create");
        DepartmentResponse newDepartment = departmentCUService.createDepartment(request);
        return ResponseEntity.ok().body(newDepartment);
    }

    @PutMapping(value = UPDATE_DEPARTMENT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepartmentResponse> updateDepartment(@RequestParam Long id, @RequestBody DepartmentRequest request) {
        LOGGER.info("[POST]{}/ update a department", PMSConstants.PREFIX_URL + "/department/update?id=" + id);
        DepartmentResponse updateDepartment = departmentCUService.updateDepartment(id, request);
        return ResponseEntity.ok().body(updateDepartment);
    }

    @DeleteMapping(value = DELETE_DEPARTMENT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteDepartment (@RequestParam Long id) {
        LOGGER.info("[POST]{}/ delete a department", PMSConstants.PREFIX_URL + "/department/delete?id=" + id);
        departmentCUService.deleteDepartmentById(id);
        return ResponseEntity.ok().body("\"Department deleted successfully!\"");
    }
}
