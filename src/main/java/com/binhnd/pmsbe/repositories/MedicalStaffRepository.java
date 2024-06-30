package com.binhnd.pmsbe.repositories;

import com.binhnd.pmsbe.entity.MedicalStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalStaffRepository extends JpaRepository<MedicalStaff, Long> {

    List<MedicalStaff> findMedicalStaffByAccountIsNull();

    List<MedicalStaff> findAllByDepartmentIsNotNull();

}
