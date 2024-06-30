package com.binhnd.pmsbe.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Table(name = "PMS_PATIENT_ROOM")
@Entity
public class PatientRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "ROOM_NUMBER", nullable = false)
    private String roomNumber;

    @Column(name = "ROOM_CODE", nullable = false, unique = true)
    private String roomCode;

    @Column(name = "TOTAL_BED", nullable = false)
    private Long totalBed;

    @Column(name = "STATUS", nullable = false, columnDefinition = "0")
    private Boolean status;

    @Column(name = "CREATE_BY", updatable = false)
    private String createBy;

    @Column(name = "UPDATE_BY")
    private String updateBy;

    @Column(name = "CREATE_TIME", updatable = false)
    private Timestamp createTime;

    @Column(name = "UPDATE_TIME")
    private Timestamp updateTime;

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID", nullable = false)
    private Department department;

    @OneToMany(mappedBy = "room")
    private List<StaffRoom> rooms;

    @OneToMany(mappedBy = "patientRoom")
private List<PatientBed> beds;
}
