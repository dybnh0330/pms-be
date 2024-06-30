package com.binhnd.pmsbe.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "PMS_RESULT")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "FILE_NAME", nullable = false)
    private String fileName;

    @Column(name = "FILE_URL", nullable = false)
    private String fileUrl;

    @Column(name = "FILE_TYPE", nullable = false)
    private String fileType;

    @Column(name = "CREATE_TIME", nullable = false, updatable = false)
    private Timestamp createTime;

    @Column(name = "CREATE_BY", nullable = false, updatable = false)
    private String createBy;

    @ManyToOne
    @JoinColumn(name = "RECORD_ID", nullable = false)
    private MedicalRecord medicalRecord;

}
