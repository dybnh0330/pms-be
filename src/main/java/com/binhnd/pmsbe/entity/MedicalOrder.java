package com.binhnd.pmsbe.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Table(name = "PMS_MEDICAL_ORDER")
public class MedicalOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CREATE_TIME", updatable = false)
    private Timestamp createTime;

    @Column(name = "CREATE_BY", updatable = false)
    private String createBy;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PATIENT_ID", referencedColumnName = "ID", nullable = false)
    private Patient patient;

    @OneToMany(mappedBy = "medicalOrder")
    private List<MedicalOrderDetails> medicalOrderDetails;
}
