package com.binhnd.pmsbe.services.department;

import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.DepartmentResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DepartmentService {

    List<DepartmentResponse> findAllDepartment();

    Page<DepartmentResponse> findAllPageDepartment(SearchSortPageableDTO dto);

    DepartmentResponse findDepartmentById(Long id);

}
