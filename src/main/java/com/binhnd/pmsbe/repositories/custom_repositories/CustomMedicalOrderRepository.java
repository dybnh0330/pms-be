package com.binhnd.pmsbe.repositories.custom_repositories;

import com.binhnd.pmsbe.common.constants.DepartmentConstant;
import com.binhnd.pmsbe.common.constants.MedicalStaffConstant;
import com.binhnd.pmsbe.entity.MedicalOrderDetails;
import com.binhnd.pmsbe.entity.MedicalStaff;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.List;

import static com.binhnd.pmsbe.common.constants.MedicalOrderConstant.*;

@Repository
public class CustomMedicalOrderRepository {

    @PersistenceContext
    private EntityManager em;

    public List<MedicalOrderDetails> findAllMedicalOrderDetail(Long medicalOrderId, Long typeId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MedicalOrderDetails> query = cb.createQuery(MedicalOrderDetails.class);
        Root<MedicalOrderDetails> root = query.from(MedicalOrderDetails.class);

        Predicate orderId = cb.equal(root.get(MEDICAL_ORDER).get(MEDICAL_ORDER_ID), medicalOrderId);

        Predicate type = cb.equal(root.get(TYPE), typeId);

        Predicate medicalOrderDetails = cb.and(orderId, type);

        CriteriaQuery<MedicalOrderDetails> response = query.where(medicalOrderDetails);

        TypedQuery<MedicalOrderDetails> orderDetailsTypedQuery = em.createQuery(response);

        return orderDetailsTypedQuery.getResultList();

    }

}
