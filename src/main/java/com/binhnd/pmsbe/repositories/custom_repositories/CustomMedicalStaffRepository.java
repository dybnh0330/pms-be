package com.binhnd.pmsbe.repositories.custom_repositories;

import com.binhnd.pmsbe.common.constants.DepartmentConstant;
import com.binhnd.pmsbe.common.constants.MedicalStaffConstant;
import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.dto.request.MedicalStaffRequest;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.MedicalStaffResponse;
import com.binhnd.pmsbe.dto.response.StaffPerDepartment;
import com.binhnd.pmsbe.entity.MedicalStaff;
import com.binhnd.pmsbe.mapper.MedicalStaffMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static com.binhnd.pmsbe.common.constants.MedicalStaffConstant.*;
import static com.binhnd.pmsbe.common.constants.PMSConstants.ASC;

@Repository
public class CustomMedicalStaffRepository {

    @PersistenceContext
    private EntityManager em;

    public List<MedicalStaff> findAllMedicalStaffByDepartmentId(Long departmentId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MedicalStaff> query = cb.createQuery(MedicalStaff.class);
        Root<MedicalStaff> root = query.from(MedicalStaff.class);

        Predicate medicalStaffByDepartmentId = cb.equal(root.get(MedicalStaffConstant.DEPARTMENT).get(DepartmentConstant.ID), departmentId);

        CriteriaQuery<MedicalStaff> response = query.where(medicalStaffByDepartmentId);

        TypedQuery<MedicalStaff> medicalStaffs = em.createQuery(response);

        return medicalStaffs.getResultList();
    }

    public List<MedicalStaff> findAllDoctorByDepartment(Long departmentId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MedicalStaff> query = cb.createQuery(MedicalStaff.class);
        Root<MedicalStaff> root = query.from(MedicalStaff.class);

        Predicate medicalStaffByDepartmentId = cb.equal(root.get(MedicalStaffConstant.DEPARTMENT).get(DepartmentConstant.ID), departmentId);

        Predicate doctorInDepartment = cb.equal(root.get(SPECIALIZE), PMSConstants.DOCTOR);

        Predicate allDoctorInDepartment = cb.and(medicalStaffByDepartmentId, doctorInDepartment);

        CriteriaQuery<MedicalStaff> response = query.where(allDoctorInDepartment);

        TypedQuery<MedicalStaff> medicalStaffs = em.createQuery(response);

        return medicalStaffs.getResultList();
    }

    public Page<MedicalStaffResponse> findAllMedicalStaffPage(SearchSortPageableDTO dto) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MedicalStaff> query = cb.createQuery(MedicalStaff.class);
        Root<MedicalStaff> root = query.from(MedicalStaff.class);

        Predicate searchMedicalStaff = cb.or(
                cb.like(root.get(NAME), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(CERTIFICATE), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(SPECIALIZE), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(DEPARTMENT).get(DepartmentConstant.NAME), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(ADDRESS), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(EMAIL), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(PHONE_NUMBER), "%" + dto.getSearchText() + "%"));

        if (!ObjectUtils.isEmpty(dto.getSortColumn()) && !ObjectUtils.isEmpty(dto.getSortDirection())) {
            if (StringUtil.uppercaseAndRemoveWhitespace(dto.getSortDirection()).equals(ASC.toUpperCase())) {
                query.orderBy(cb.asc(root.get(dto.getSortColumn())));
            } else {
                query.orderBy(cb.desc(root.get(dto.getSortColumn())));
            }
        }

        Pageable pageable = PageRequest.of(dto.getCurrentPageNumber(), dto.getPageSize());

        query.where(searchMedicalStaff);
        TypedQuery<MedicalStaff> medicalStaffTypedQuery = em.createQuery(query);
        medicalStaffTypedQuery.setFirstResult((int) pageable.getOffset());
        medicalStaffTypedQuery.setMaxResults(pageable.getPageSize());

        List<MedicalStaff> medicalStaffs = medicalStaffTypedQuery.getResultList();

        CriteriaQuery<Long> countMedicalStaff = cb.createQuery(Long.class);
        countMedicalStaff.select(cb.count(countMedicalStaff.from(MedicalStaff.class)));
        countMedicalStaff.where(searchMedicalStaff);

        TypedQuery<Long> countTypedQuery = em.createQuery(countMedicalStaff);
        long totalMedicalStaff = countTypedQuery.getSingleResult();

        List<MedicalStaffResponse> responses = MedicalStaffMapper.mapAll(medicalStaffs);

        return new PageImpl<>(responses, pageable, totalMedicalStaff);
    }

    public Page<MedicalStaffResponse> findAllMedicalStaffPageByDepartmentId(SearchSortPageableDTO dto, Long departmentId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MedicalStaff> query = cb.createQuery(MedicalStaff.class);
        Root<MedicalStaff> root = query.from(MedicalStaff.class);

        Predicate medicalStaffByDepartmentId = cb.equal(root.get(MedicalStaffConstant.DEPARTMENT).get(DepartmentConstant.ID), departmentId);

        Predicate searchMedicalStaff = cb.or(
                cb.like(root.get(NAME), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(CERTIFICATE), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(SPECIALIZE), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(DEPARTMENT).get(DepartmentConstant.NAME), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(ADDRESS), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(EMAIL), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(PHONE_NUMBER), "%" + dto.getSearchText() + "%"));

        Predicate medicalStaffWithSearchText = cb.and(medicalStaffByDepartmentId, searchMedicalStaff);

        if (!ObjectUtils.isEmpty(dto.getSortColumn()) && !ObjectUtils.isEmpty(dto.getSortDirection())) {
            if (StringUtil.uppercaseAndRemoveWhitespace(dto.getSortDirection()).equals(ASC.toUpperCase())) {
                query.orderBy(cb.asc(root.get(dto.getSortColumn())));
            } else {
                query.orderBy(cb.desc(root.get(dto.getSortColumn())));
            }
        }

        Pageable pageable = PageRequest.of(dto.getCurrentPageNumber(), dto.getPageSize());

        query.where(medicalStaffWithSearchText);
        TypedQuery<MedicalStaff> medicalStaffTypedQuery = em.createQuery(query);
        medicalStaffTypedQuery.setFirstResult((int) pageable.getOffset());
        medicalStaffTypedQuery.setMaxResults(pageable.getPageSize());

        List<MedicalStaff> medicalStaffs = medicalStaffTypedQuery.getResultList();

        CriteriaQuery<Long> countMedicalStaff = cb.createQuery(Long.class);
        countMedicalStaff.select(cb.count(countMedicalStaff.from(MedicalStaff.class)));
        countMedicalStaff.where(searchMedicalStaff, medicalStaffByDepartmentId);

        TypedQuery<Long> countTypedQuery = em.createQuery(countMedicalStaff);
        long totalMedicalStaff = countTypedQuery.getSingleResult();

        List<MedicalStaffResponse> responses = MedicalStaffMapper.mapAll(medicalStaffs);

        return new PageImpl<>(responses, pageable, totalMedicalStaff);
    }

    public MedicalStaff findMedicalStaffExisted(MedicalStaffRequest request) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MedicalStaff> query = cb.createQuery(MedicalStaff.class);
        Root<MedicalStaff> root = query.from(MedicalStaff.class);

        Predicate cccd = cb.equal(root.get(CCCD), request.getCccd());
        Predicate phoneNumber = cb.equal(root.get(PHONE_NUMBER), request.getPhoneNumber());
        Predicate email = cb.equal(root.get(EMAIL), request.getEmail());
        Predicate certificate = cb.equal(root.get(CERTIFICATE), request.getCertificate());

        Predicate medicalStaffExisted = cb.or(cccd, phoneNumber, email, certificate);

        CriteriaQuery<MedicalStaff> medicalStaffCriteriaQuery = query.where(medicalStaffExisted);

        TypedQuery<MedicalStaff> medicalStaffTypedQuery = em.createQuery(medicalStaffCriteriaQuery);

        List<MedicalStaff> medicalStaffs = medicalStaffTypedQuery.getResultList();

        if (medicalStaffs.isEmpty()) {
            return null;
        }

        return medicalStaffs.get(0);
    }

    public Long countMedicalStaff() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countMedicalStaff = cb.createQuery(Long.class);

        Root<MedicalStaff> root = countMedicalStaff.from(MedicalStaff.class);

        Predicate medicalStaffDepartmentNotNull = cb.isNotNull(root.get(MedicalStaffConstant.DEPARTMENT));

        countMedicalStaff.select(cb.count(root));
        countMedicalStaff.where(medicalStaffDepartmentNotNull);

        TypedQuery<Long> countTypedQuery = em.createQuery(countMedicalStaff);

        return countTypedQuery.getSingleResult();
    }

    public List<StaffPerDepartment> countMedicalStaffPerDepartment() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<MedicalStaff> medicalStaffRoot = query.from(MedicalStaff.class);
//        Root<Department> departmentRoot = query.from(Department.class);

        Expression<Long> count = cb.count(medicalStaffRoot.get(ID));
        Expression<String> groupBy = medicalStaffRoot.get(DEPARTMENT).get(NAME);
        query.multiselect(count, groupBy);

        medicalStaffRoot.join(DEPARTMENT, JoinType.INNER);

        query.groupBy(groupBy);

        List<Object[]> results = em.createQuery(query).getResultList();

        List<StaffPerDepartment> staffPerDepartments = new ArrayList<>();

        for (Object[] row: results) {
            StaffPerDepartment staffPerDepartment = new StaffPerDepartment();
            staffPerDepartment.setDepartmentName(row[1].toString());
            staffPerDepartment.setTotalStaffInDepartment((Long) row[0]);
            staffPerDepartments.add(staffPerDepartment);
        }

        return staffPerDepartments;
    }
}
