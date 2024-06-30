package com.binhnd.pmsbe.repositories;

import com.binhnd.pmsbe.entity.MedicalRecord;
import com.binhnd.pmsbe.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findResultByMedicalRecord(MedicalRecord details);

}
