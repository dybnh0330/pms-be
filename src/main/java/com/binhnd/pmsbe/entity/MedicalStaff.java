package com.binhnd.pmsbe.entity;

import com.binhnd.pmsbe.common.enums.Account;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Table(name = "PMS_MEDICAL_STAFF")
public class MedicalStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "GENDER", nullable = false)
    private Long gender;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Column(name = "DOB", nullable = false)
    private Timestamp dob;

    @Column(name = "CCCD", unique = true, nullable = false)
    private String cccd;

    @Column(name = "PHONE_NUMBER", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "CERTIFICATE", unique = true)
    private String certificate;

    @Column(name = "SPECIALIZE")
    private String specialize;

    @Column(name = "CREATE_TIME", updatable = false)
    private Timestamp createTime;

    @Column(name = "UPDATE_TIME")
    private Timestamp updateTime;

    @Column(name = "CREATE_BY", updatable = false)
    private String createBy;

    @Column(name = "UPDATE_BY")
    private String updateBy;

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department;

    @OneToMany(mappedBy = "medicalStaff")
    private List<Patient> patients;

    @OneToOne(mappedBy = "medicalStaff")
    private Account account;

    @OneToMany(mappedBy = "nurse")
    private List<StaffRoom> nurses;
}

