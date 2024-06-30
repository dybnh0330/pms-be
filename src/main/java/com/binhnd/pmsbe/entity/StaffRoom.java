package com.binhnd.pmsbe.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "PMS_STAFF_ROOM")
public class StaffRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

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
    private PatientRoom room;

    @ManyToOne
    @JoinColumn(name = "NURSE_ID", nullable = false)
    private MedicalStaff nurse;
}
