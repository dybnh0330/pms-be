package com.binhnd.pmsbe.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "PMS_PATIENT_BED")
@Entity
public class PatientBed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "BED_CODE", nullable = false)
    private String bedCode;

    @Column(name = "BED_NUMBER", nullable = false, unique = true)
    private String bedNumber;

    @Column(name = "STATUS", nullable = false, columnDefinition = "0")
    private Boolean status;

    @Column(name = "CREATE_TIME", updatable = false)
    private Timestamp createTime;

    @Column(name = "UPDATE_TIME")
    private Timestamp updateTime;

    @Column(name = "CREATE_BY", updatable = false)
    private String createBy;

    @Column(name = "UPDATE_BY")
    private String updateBy;

    @ManyToOne
    @JoinColumn(name = "ROOM_ID", nullable = false)
    private PatientRoom patientRoom;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PATIENT_ID", referencedColumnName = "ID")
    private Patient patient;
}
