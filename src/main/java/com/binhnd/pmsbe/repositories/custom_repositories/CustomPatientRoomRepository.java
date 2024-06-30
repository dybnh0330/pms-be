package com.binhnd.pmsbe.repositories.custom_repositories;

import com.binhnd.pmsbe.common.constants.DepartmentConstant;
import com.binhnd.pmsbe.common.constants.PatientRoomConstant;
import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.dto.request.PatientRoomHasBedRequest;
import com.binhnd.pmsbe.dto.request.PatientRoomRequest;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.PatientRoomResponse;
import com.binhnd.pmsbe.entity.PatientRoom;
import com.binhnd.pmsbe.mapper.PatientRoomMapper;
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
import static com.binhnd.pmsbe.common.constants.PatientRoomConstant.*;

@Repository
public class CustomPatientRoomRepository {

    @PersistenceContext
    private EntityManager em;

    public List<PatientRoom> findAllPatientRoomByDepartment (Long departmentId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PatientRoom> query = cb.createQuery(PatientRoom.class);
        Root<PatientRoom> root = query.from(PatientRoom.class);

        Predicate patientRoom = cb.equal(root.get(PatientRoomConstant.DEPARTMENT).get(DepartmentConstant.ID), departmentId);

        CriteriaQuery<PatientRoom> response = query.where(patientRoom);

        TypedQuery<PatientRoom> patientRooms = em.createQuery(response);

        return patientRooms.getResultList();
    }

    public List<PatientRoom> findAllPatientRoomEmptyByDepartment (Long departmentId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PatientRoom> query = cb.createQuery(PatientRoom.class);
        Root<PatientRoom> root = query.from(PatientRoom.class);

        Predicate department = cb.equal(root.get(PatientRoomConstant.DEPARTMENT).get(DepartmentConstant.ID), departmentId);

        Predicate status = cb.equal(root.get(STATUS), true);

        Predicate patientRoom = cb.and(department, status);

        CriteriaQuery<PatientRoom> response = query.where(patientRoom);

        TypedQuery<PatientRoom> patientRooms = em.createQuery(response);

        return patientRooms.getResultList();
    }

    public Page<PatientRoomResponse> findAllPatientRoomByDeparmentPage(Long departmentId, SearchSortPageableDTO dto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PatientRoom> query = cb.createQuery(PatientRoom.class);
        Root<PatientRoom> root = query.from(PatientRoom.class);

        Predicate department = cb.equal(root.get(DEPARTMENT).get(DepartmentConstant.ID), departmentId);

        Predicate searchPatientRoom =  cb.like(root.get(ROOM_NUMBER), "%" + dto.getSearchText() + "%");

        Predicate patientRoomByDepartment = cb.and(department, searchPatientRoom);

        if (!ObjectUtils.isEmpty(dto.getSortColumn()) && !ObjectUtils.isEmpty(dto.getSortDirection())) {
            if (StringUtil.uppercaseAndRemoveWhitespace(dto.getSortDirection()).equals(ASC.toUpperCase())) {
                query.orderBy(cb.asc(root.get(dto.getSortColumn())));
            } else {
                query.orderBy(cb.desc(root.get(dto.getSortColumn())));
            }
        }

        Pageable pageable = PageRequest.of(dto.getCurrentPageNumber(), dto.getPageSize());

        query.where(patientRoomByDepartment);
        TypedQuery<PatientRoom> patientRoomTypedQuery = em.createQuery(query);
        patientRoomTypedQuery.setFirstResult((int) pageable.getOffset());
        patientRoomTypedQuery.setMaxResults(pageable.getPageSize());

        List<PatientRoom> patientRooms = patientRoomTypedQuery.getResultList();

        CriteriaQuery<Long> countPatientRoom = cb.createQuery(Long.class);
        countPatientRoom.select(cb.count(countPatientRoom.from(PatientRoom.class)));
        countPatientRoom.where(searchPatientRoom, department);

        TypedQuery<Long> countTypedQuery = em.createQuery(countPatientRoom);
        long totalPatient = countTypedQuery.getSingleResult();

        List<PatientRoomResponse> responses = PatientRoomMapper.mapAll(patientRooms);

        return new PageImpl<>(responses, pageable, totalPatient);
    }

    public PatientRoom findPatientRoomExisted (PatientRoomRequest request) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PatientRoom> query = cb.createQuery(PatientRoom.class);
        Root<PatientRoom> root = query.from(PatientRoom.class);

        Predicate department = cb.equal(root.get(DEPARTMENT).get(DepartmentConstant.ID), request.getDepartmentId());

        Predicate roomNumber = cb.equal(root.get(ROOM_NUMBER), request.getRoomNumber());

        Predicate patientRoom = cb.and(department, roomNumber);

        CriteriaQuery<PatientRoom> patientRoomCriteriaQuery = query.where(patientRoom);

        TypedQuery<PatientRoom> patientRoomTypedQuery = em.createQuery(patientRoomCriteriaQuery);

        List<PatientRoom> patientRooms = patientRoomTypedQuery.getResultList();

        if (patientRooms.isEmpty()) {
            return null;
        }

        return patientRooms.get(0);
    }

    public PatientRoom findPatientRoomExisted (PatientRoomHasBedRequest request) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PatientRoom> query = cb.createQuery(PatientRoom.class);
        Root<PatientRoom> root = query.from(PatientRoom.class);

        Predicate department = cb.equal(root.get(DEPARTMENT).get(DepartmentConstant.ID), request.getDepartmentId());

        Predicate roomNumber = cb.equal(root.get(ROOM_NUMBER), request.getRoomNumber());

        Predicate patientRoom = cb.and(department, roomNumber);

        CriteriaQuery<PatientRoom> patientRoomCriteriaQuery = query.where(patientRoom);

        TypedQuery<PatientRoom> patientRoomTypedQuery = em.createQuery(patientRoomCriteriaQuery);

        List<PatientRoom> patientRooms = patientRoomTypedQuery.getResultList();

        if (patientRooms.isEmpty()) {
            return null;
        }

        return patientRooms.get(0);
    }
}
