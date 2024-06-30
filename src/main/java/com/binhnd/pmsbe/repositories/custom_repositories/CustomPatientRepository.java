package com.binhnd.pmsbe.repositories.custom_repositories;

import com.binhnd.pmsbe.common.constants.DepartmentConstant;
import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.dto.request.PatientInfoRequest;
import com.binhnd.pmsbe.dto.request.SearchSortDto;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.PatientResponse;
import com.binhnd.pmsbe.entity.Patient;
import com.binhnd.pmsbe.mapper.PatientMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

import static com.binhnd.pmsbe.common.constants.PMSConstants.ASC;
import static com.binhnd.pmsbe.common.constants.PatientConstant.*;

@Repository
public class CustomPatientRepository {

    @PersistenceContext
    //cung cấp API để tương tác với DB
    private EntityManager em;

    public Patient findPatientExisted(PatientInfoRequest request) {
        //obj cb sử dụng để truy vấn
        CriteriaBuilder cb = em.getCriteriaBuilder();
        //obj query sử dụng để xây dựng truy vấn theo các yêu cầu
        CriteriaQuery<Patient> query = cb.createQuery(Patient.class);
        //thiết lập gốc của truy vấn là Patient - truy vấn dựa trên class này
        Root<Patient> root = query.from(Patient.class);

        //tạo điều kiện(Predicate) để so sánh bhytCode
        Predicate bhyt = cb.equal(root.get(BHYT_CODE), request.getBhytCode());
        Predicate cccd = cb.equal(root.get(CCCD_NUMBER), request.getCccdNumber());
        Predicate patientPhone = cb.equal(root.get(PATIENT_PHONE), request.getPatientPhone());

        Predicate patientExisted = cb.or(bhyt, cccd, patientPhone);

        //tạo truy vấn với điều kiện patientExisted là tổng hợp của 3 điều kiện nhỏ trên kia
        CriteriaQuery<Patient> patientCriteriaQuery = query.where(patientExisted);

        //chạy truy vấn và lấy kết quả
        TypedQuery<Patient> patientTypedQuery = em.createQuery(patientCriteriaQuery);

        List<Patient> patients = patientTypedQuery.getResultList();

        if (patients.isEmpty()) {
            return null;
        }

        return patients.get(0);
    }

    public List<Patient> findAllPatientInDepartment(Long departmentId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Patient> query = cb.createQuery(Patient.class);
        Root<Patient> root = query.from(Patient.class);

        Predicate patientInDepartment = cb.equal(root.get(DEPARTMENT).get(DepartmentConstant.ID), departmentId);

        CriteriaQuery<Patient> response = query.where(patientInDepartment);
        TypedQuery<Patient> patientTypedQuery = em.createQuery(response);

        return patientTypedQuery.getResultList();
    }

    public List<Patient> findPatientByDepartmentIsNull(SearchSortDto dto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Patient> query = cb.createQuery(Patient.class);
        Root<Patient> root = query.from(Patient.class);

        Predicate patientDepartmentNull = cb.isNull(root.get(DEPARTMENT));

        Predicate searchPatient = cb.or(
                cb.like(root.get(NAME), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(ADDRESS), "%" + dto.getSearchText() + "%"));


        CriteriaQuery<Patient> response = query.where(patientDepartmentNull, searchPatient);

        if (!ObjectUtils.isEmpty(dto.getSortColumn()) && !ObjectUtils.isEmpty(dto.getSortDirection())) {
            if (StringUtil.uppercaseAndRemoveWhitespace(dto.getSortDirection()).equals(ASC.toUpperCase())) {
                query.orderBy(cb.asc(root.get(dto.getSortColumn())));
            } else {
                query.orderBy(cb.desc(root.get(dto.getSortColumn())));
            }
        }

        TypedQuery<Patient> patientTypedQuery = em.createQuery(response);

        return patientTypedQuery.getResultList();
    }

    public Page<PatientResponse> findAllPatientInDepartmentPage(Long departmentId, SearchSortPageableDTO dto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Patient> query = cb.createQuery(Patient.class);
        Root<Patient> root = query.from(Patient.class);

        Predicate searchPatient = cb.or(
                cb.like(root.get(NAME), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(DEPARTMENT).get(DepartmentConstant.NAME), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(CCCD_NUMBER), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(PATIENT_PHONE), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(GUARDIAN_PHONE), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(ADDRESS), "%" + dto.getSearchText() + "%"));

        Predicate patientInDepartment = cb.equal(root.get(DEPARTMENT).get(DepartmentConstant.ID), departmentId);
        Predicate patientBeingTreated = cb.isNotNull(root.get(MEDICAL_STAFF));

        Predicate patientWithSearchText = cb.and(searchPatient, patientInDepartment, patientBeingTreated);

        if (!ObjectUtils.isEmpty(dto.getSortColumn()) && !ObjectUtils.isEmpty(dto.getSortDirection())) {
            if (StringUtil.uppercaseAndRemoveWhitespace(dto.getSortDirection()).equals(ASC.toUpperCase())) {
                query.orderBy(cb.asc(root.get(dto.getSortColumn())));
            } else {
                query.orderBy(cb.desc(root.get(dto.getSortColumn())));
            }
        }
        Pageable pageable = PageRequest.of(dto.getCurrentPageNumber(), dto.getPageSize());

        query.where(patientWithSearchText);
        TypedQuery<Patient> patientTypedQuery = em.createQuery(query);
        patientTypedQuery.setFirstResult((int) pageable.getOffset());
        patientTypedQuery.setMaxResults(pageable.getPageSize());

        List<Patient> patients = patientTypedQuery.getResultList();

        CriteriaQuery<Long> countPatient = cb.createQuery(Long.class);
        countPatient.select(cb.count(countPatient.from(Patient.class)));
        countPatient.where(searchPatient);

        TypedQuery<Long> countTypedQuery = em.createQuery(countPatient);
        long totalPatient = countTypedQuery.getSingleResult();

        List<PatientResponse> responses = PatientMapper.mapAll(patients);

        return new PageImpl<>(responses, pageable, totalPatient);
    }

    public Long countPatient() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Long> countPatient = cb.createQuery(Long.class);
        countPatient.select(cb.count(countPatient.from(Patient.class)));

        TypedQuery<Long> countTypedQuery = em.createQuery(countPatient);

        return countTypedQuery.getSingleResult();
    }

    public Long countPatientByStatus(Long status) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Patient> query = cb.createQuery(Patient.class);
        Root<Patient> root = query.from(Patient.class);

        Predicate patientByStatus = cb.equal(root.get(STATUS), status);

        CriteriaQuery<Long> countPatient = cb.createQuery(Long.class);
        countPatient.select(cb.count(countPatient.from(Patient.class)));
        countPatient.where(patientByStatus);

        TypedQuery<Long> countTypedQuery = em.createQuery(countPatient);

        return countTypedQuery.getSingleResult();
    }
}
