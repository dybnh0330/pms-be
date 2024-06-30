package com.binhnd.pmsbe.repositories;

import com.binhnd.pmsbe.entity.MedicalOrder;
import com.binhnd.pmsbe.entity.MedicalOrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalOrderDetailsRepository extends JpaRepository<MedicalOrderDetails, Long> {

    @Query(value = "select details from MedicalOrderDetails details where details.medicalOrder.id = :id")
    List<MedicalOrderDetails> findMedicalOrderDetailsByMedicalOrder(@Param("id") Long medicalOrderId);

}
