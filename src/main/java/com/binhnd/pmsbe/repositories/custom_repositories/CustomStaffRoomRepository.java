package com.binhnd.pmsbe.repositories.custom_repositories;

import com.binhnd.pmsbe.common.constants.PatientRoomConstant;
import com.binhnd.pmsbe.entity.PatientRoom;
import com.binhnd.pmsbe.entity.StaffRoom;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

import static com.binhnd.pmsbe.common.constants.StaffRoomConstant.ROOM;

@Repository
public class CustomStaffRoomRepository {

    @PersistenceContext
    private EntityManager em;

    public List<StaffRoom> findAllStaffInRoom (Long roomId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<StaffRoom> query = cb.createQuery(StaffRoom.class);
        Root<StaffRoom> root = query.from(StaffRoom.class);

        Predicate staffInRoom = cb.equal(root.get(ROOM).get(PatientRoomConstant.ID), roomId);

        CriteriaQuery<StaffRoom> staffRoomCriteriaQuery = query.where(staffInRoom);

        TypedQuery<StaffRoom> staffRoomTypedQuery = em.createQuery(staffRoomCriteriaQuery);

        return staffRoomTypedQuery.getResultList();
    }

}
