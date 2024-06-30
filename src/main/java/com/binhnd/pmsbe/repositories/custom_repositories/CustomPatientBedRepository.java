package com.binhnd.pmsbe.repositories.custom_repositories;

import com.binhnd.pmsbe.common.constants.PatientRoomConstant;
import com.binhnd.pmsbe.dto.request.PatientBedRequest;
import com.binhnd.pmsbe.entity.PatientBed;
import com.binhnd.pmsbe.entity.PatientRoom;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

import static com.binhnd.pmsbe.common.constants.PatientBedConstant.*;

@Repository
public class CustomPatientBedRepository {

    @PersistenceContext
    private EntityManager em;

    public List<PatientBed> findAllPatientBedByRoomId(Long roomId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PatientBed> query = cb.createQuery(PatientBed.class);
        Root<PatientBed> root = query.from(PatientBed.class);

        Predicate patientBed = cb.equal(root.get(PATIENT_ROOM).get(PatientRoomConstant.ID), roomId);

        CriteriaQuery<PatientBed> response = query.where(patientBed);
        TypedQuery<PatientBed> patientBedTypedQuery = em.createQuery(response);

        return patientBedTypedQuery.getResultList();
    }

    public List<PatientBed> findAllBedEmptyByRoom(Long roomId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PatientBed> query = cb.createQuery(PatientBed.class);
        Root<PatientBed> root = query.from(PatientBed.class);

        Predicate patientBed = cb.equal(root.get(PATIENT_ROOM).get(PatientRoomConstant.ID), roomId);

        Predicate bedEmpty = cb.equal(root.get(STATUS), true);

        Predicate allBedEmptyBeRoom = cb.and(patientBed, bedEmpty);

        CriteriaQuery<PatientBed> response = query.where(allBedEmptyBeRoom);
        TypedQuery<PatientBed> patientBedTypedQuery = em.createQuery(response);

        return patientBedTypedQuery.getResultList();
    }

    public PatientBed findPatientBedExisted (PatientBedRequest request) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PatientBed> query = cb.createQuery(PatientBed.class);
        Root<PatientBed> root = query.from(PatientBed.class);

        Predicate room = cb.equal(root.get(PATIENT_ROOM).get(PatientRoomConstant.ID), request.getRoomId());

        Predicate bedNumber = cb.equal(root.get(BED_NUMBER), request.getBedNumber());

        Predicate patientBed = cb.and(room, bedNumber);

        CriteriaQuery<PatientBed> patientBedCriteriaQuery = query.where(patientBed);

        TypedQuery<PatientBed> patientBedTypedQuery = em.createQuery(patientBedCriteriaQuery);

        List<PatientBed> patientBeds = patientBedTypedQuery.getResultList();

        if (patientBeds.isEmpty()) {
            return null;
        }

        return patientBeds.get(0);
    }
}
