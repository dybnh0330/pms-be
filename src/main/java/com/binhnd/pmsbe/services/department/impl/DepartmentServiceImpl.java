package com.binhnd.pmsbe.services.department.impl;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.common.utils.ObjectMapperUtils;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.DepartmentResponse;
import com.binhnd.pmsbe.entity.Department;
import com.binhnd.pmsbe.repositories.DepartmentRepository;
import com.binhnd.pmsbe.repositories.custom_repositories.CustomDepartmentRepository;
import com.binhnd.pmsbe.services.department.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final CustomDepartmentRepository customDepartmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository,
                                 CustomDepartmentRepository customDepartmentRepository) {
        this.departmentRepository = departmentRepository;
        this.customDepartmentRepository = customDepartmentRepository;
    }

    @Override
    public List<DepartmentResponse> findAllDepartment() {
        List<Department> departments = departmentRepository.findAll();

        return ObjectMapperUtils.mapAll(departments, DepartmentResponse.class);
    }

    @Override
    public Page<DepartmentResponse> findAllPageDepartment(SearchSortPageableDTO dto) {

        return customDepartmentRepository.findAllDepartment(dto);
    }

    @Override
    public DepartmentResponse findDepartmentById(Long id) {

        Optional<Department> departmentByID = departmentRepository.findById(id);
        if (departmentByID.isEmpty()) {
            throw new PMSException(EnumPMSException.DEPARTMENT_NOT_EXIST);
        }

        return ObjectMapperUtils.map(departmentByID.get(), DepartmentResponse.class);
    }
}
