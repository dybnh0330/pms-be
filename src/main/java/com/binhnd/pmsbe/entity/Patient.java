package com.binhnd.pmsbe.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Data
@Table(name = "PMS_PATIENT")
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "PATIENT_CODE", nullable = false, unique = true)
    private String patientCode;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "GENDER", nullable = false)
    private Long gender;

    @Column(name = "DOB", nullable = false)
    private Timestamp dateOfBirth;

    @Column(name = "AGE", nullable = false)
    private Long age;

    @Column(name = "BHYT_CODE", nullable = false, unique = true)
    private String bhytCode;

    @Column(name = "CCCD_NUMBER", nullable = false, unique = true)
    private String cccdNumber;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Column(name = "PATIENT_PHONE", unique = true)
    private String patientPhone;

    @Column(name = "GUARDIAN_PHONE")
    private String guardianPhone;

    @Column(name = "ADMISSION_TIME", updatable = false)
    private Timestamp admissionTime;

    @Column(name = "DISCHARGE_TIME")
    private Timestamp dischargeTime;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "UPDATE_TIME")
    private Timestamp updateTime;

    @Column(name = "CREATE_BY", updatable = false)
    private String createBy;

    @Column(name = "UPDATE_BY")
    private String updateBy;

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "MEDICAL_STAFF_ID")
    private MedicalStaff medicalStaff;

    @OneToOne(mappedBy = "patient")
    private PatientBed patientBed;

    @OneToOne(mappedBy = "patient")
    private MedicalRecord medicalRecord;

    @OneToOne(mappedBy = "patient")
    private MedicalOrder medicalOrder;
}
