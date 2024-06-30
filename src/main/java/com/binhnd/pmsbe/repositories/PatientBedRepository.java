package com.binhnd.pmsbe.repositories;

import com.binhnd.pmsbe.entity.PatientBed;
import com.binhnd.pmsbe.entity.PatientRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientBedRepository extends JpaRepository<PatientBed, Long> {

    PatientBed findPatientBedByBedNumberAndPatientRoom(String bedNumber, PatientRoom patientRoom);

}
