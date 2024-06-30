package com.binhnd.pmsbe.repositories;

import com.binhnd.pmsbe.entity.MedicalOrder;
import com.binhnd.pmsbe.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalOrderRepository extends JpaRepository<MedicalOrder, Long> {
    MedicalOrder findMedicalOrderByPatient(Patient patient);
}
