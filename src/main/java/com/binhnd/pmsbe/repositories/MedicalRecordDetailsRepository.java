package com.binhnd.pmsbe.repositories;

import com.binhnd.pmsbe.entity.MedicalRecord;
import com.binhnd.pmsbe.entity.MedicalRecordDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordDetailsRepository extends JpaRepository<MedicalRecordDetails, Long> {

    List<MedicalRecordDetails> findMedicalRecordDetailsByMedicalRecord(MedicalRecord medicalRecord);

}
