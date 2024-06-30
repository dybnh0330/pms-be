package com.binhnd.pmsbe.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "PMS_ROLE")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "CREATE_TIME", updatable = false)
    private Timestamp createTime;

    @Column(name = "CREATE_BY", updatable = false, columnDefinition = "SYSTEM")
    private String createBy;

    @Column(name = "UPDATE_TIME", updatable = false)
    private Timestamp updateTime;

    @Column(name = "UPDATE_BY", updatable = false)
    private String updateBy;

}
