package com.binhnd.pmsbe.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@Table(name = "PMS_MEDICAL_RECORD")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "RECORD_CODE", nullable = false, unique = true)
    private String recordCode;

    @Column(name = "REASON", nullable = false)
    private String reason;

    @Column(name = "MEDICAL_HISTORY", nullable = false)
    private String medicalHistory;

    @Column(name = "DIAGNOSTIC")
    private String diagnostic;

    @Column(name = "CREATE_BY", updatable = false)
    private String createBy;

    @Column(name = "UPDATE_BY")
    private String updateBy;

    @Column(name = "CREATE_TIME", updatable = false)
    private Timestamp createTime;

    @Column(name = "UPDATE_TIME")
    private Timestamp updateTime;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PATIENT_ID", referencedColumnName = "ID")
    private Patient patient;

    @OneToMany(mappedBy = "medicalRecord")
    private List<MedicalRecordDetails> medicalRecordDetails;

    @OneToMany(mappedBy = "medicalRecord")
    private List<Result> results;
}
