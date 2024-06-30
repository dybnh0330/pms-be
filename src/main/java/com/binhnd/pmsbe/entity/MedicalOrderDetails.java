package com.binhnd.pmsbe.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "PMS_MEDICAL_ORDER_DETAIL")
public class MedicalOrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TYPE", nullable = false)
    private Long type;

    @Column(name = "QUANTITY", nullable = false)
    private Long quantity;

    @Column(name = "UNIT", nullable = false)
    private String unit;

    @Column(name = "NOTE", nullable = false)
    private String note;

    @Column(name = "CREATE_TIME", updatable = false)
    private Timestamp createTime;

    @Column(name = "CREATE_BY", updatable = false)
    private String createBy;

    @ManyToOne
    @JoinColumn(name = "MEDICAL_ORDER_ID", nullable = false)
    private MedicalOrder medicalOrder;

    @OneToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

}
