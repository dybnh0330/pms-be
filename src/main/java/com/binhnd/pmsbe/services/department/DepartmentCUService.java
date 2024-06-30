package com.binhnd.pmsbe.services.department;

import com.binhnd.pmsbe.dto.request.DepartmentRequest;
import com.binhnd.pmsbe.dto.response.DepartmentResponse;

import java.util.List;

public interface DepartmentCUService {

    DepartmentResponse createDepartment(DepartmentRequest request);
    DepartmentResponse updateDepartment(Long id, DepartmentRequest request);
    void deleteDepartmentById (Long id);

}
