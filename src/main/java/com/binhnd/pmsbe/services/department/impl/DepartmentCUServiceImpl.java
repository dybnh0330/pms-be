package com.binhnd.pmsbe.services.department.impl;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.common.utils.ObjectMapperUtils;
import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.common.utils.jwt.SecurityUtils;
import com.binhnd.pmsbe.dto.request.DepartmentRequest;
import com.binhnd.pmsbe.dto.response.DepartmentResponse;
import com.binhnd.pmsbe.entity.Department;
import com.binhnd.pmsbe.repositories.DepartmentRepository;
import com.binhnd.pmsbe.repositories.custom_repositories.CustomDepartmentRepository;
import com.binhnd.pmsbe.services.department.DepartmentCUService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class DepartmentCUServiceImpl implements DepartmentCUService {

    private final DepartmentRepository departmentRepository;
    private final CustomDepartmentRepository customDepartmentRepository;

    @Autowired
    public DepartmentCUServiceImpl(DepartmentRepository departmentRepository,
                                   CustomDepartmentRepository customDepartmentRepository) {
        this.departmentRepository = departmentRepository;
        this.customDepartmentRepository = customDepartmentRepository;
    }

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest request) {

        validateAndCorrectDataDepartment(request);

        Department department = new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setCreateTime(Timestamp.from(Instant.now()));
        department.setCreateBy(SecurityUtils.getCurrentUsername());
        department.setUpdateTime(Timestamp.from(Instant.now()));

        checkDuplicateDepartment(request);

        departmentRepository.save(department);

        return ObjectMapperUtils.map(department, DepartmentResponse.class);
    }

    @Override
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {

        validateAndCorrectDataDepartment(request);

        Optional<Department> department = departmentRepository.findById(id);

        if (department.isEmpty()) {
            throw new PMSException(EnumPMSException.DEPARTMENT_NOT_EXIST);
        }

        Department updateDepartment = department.get();

        if (!id.equals(request.getId())) {
            throw new PMSException(EnumPMSException.ID_NOT_EQUALS);
        }

        updateDepartment.setName(request.getName());
        updateDepartment.setDescription(request.getDescription());
        updateDepartment.setUpdateTime(Timestamp.from(Instant.now()));
        updateDepartment.setUpdateBy(SecurityUtils.getCurrentUsername());

        checkDuplicateDepartment(request);

        departmentRepository.save(updateDepartment);

        return ObjectMapperUtils.map(updateDepartment, DepartmentResponse.class);
    }

    @Override
    public void deleteDepartmentById(Long id) {

        Optional<Department> departmentById = departmentRepository.findById(id);

        if (departmentById.isEmpty()) {
            throw new PMSException(EnumPMSException.DEPARTMENT_NOT_EXIST);
        }

        Department department = departmentById.get();
        if (!department.getPatientRooms().isEmpty()) {
            throw new PMSException(EnumPMSException.DEPARTMENT_HAD_ROOM);
        }

        if (!department.getMedicalStaffs().isEmpty() || !department.getPatients().isEmpty()) {
            throw new PMSException(EnumPMSException.DEPARTMENT_HAD_PEOPLE);
        }

        departmentRepository.deleteById(id);
    }

    private void validateAndCorrectDataDepartment(DepartmentRequest request) {
        if (request == null
                || ObjectUtils.isEmpty(request.getName())
                || ObjectUtils.isEmpty(request.getDescription())) {
            throw new PMSException(EnumPMSException.DEPARTMENT_INVALID);
        }

        request.setName(StringUtil.removeWhitespace(request.getName()));
    }

    private void checkDuplicateDepartment(DepartmentRequest request) {

        Department response = customDepartmentRepository.findExistDepartment(request);
        if (!ObjectUtils.isEmpty(response) && (request.getId() == null || !request.getId().equals(response.getId()))) {
            throw new PMSException(EnumPMSException.DEPARTMENT_EXISTED);
        }
    }
}
