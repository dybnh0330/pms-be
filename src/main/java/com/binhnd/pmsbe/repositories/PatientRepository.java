package com.binhnd.pmsbe.repositories;

import com.binhnd.pmsbe.entity.Department;
import com.binhnd.pmsbe.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Patient findPatientByBhytCodeAndCccdNumberAndPatientPhone(String bhytCode, String cccdNumber, String patientPhone);

    List<Patient> findPatientByDepartmentIsNull();

    List<Patient> findPatientByMedicalStaffIsNullAndDepartmentIsNotNullAndDepartment(Department department);

    Long countPatientByStatus(Long status);

}
