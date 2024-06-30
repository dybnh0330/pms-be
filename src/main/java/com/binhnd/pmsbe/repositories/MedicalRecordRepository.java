package com.binhnd.pmsbe.repositories;

import com.binhnd.pmsbe.entity.MedicalRecord;
import com.binhnd.pmsbe.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    MedicalRecord findMedicalRecordByPatient(Patient patient);

}
