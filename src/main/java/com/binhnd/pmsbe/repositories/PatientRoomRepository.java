package com.binhnd.pmsbe.repositories;

import com.binhnd.pmsbe.entity.PatientRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRoomRepository extends JpaRepository<PatientRoom, Long> {
}
