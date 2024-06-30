package com.binhnd.pmsbe.common.enums;

import com.binhnd.pmsbe.entity.MedicalStaff;
import com.binhnd.pmsbe.entity.Role;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "PMS_ACCOUNT")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "STATUS", nullable = false)
    private Boolean status;

    @Column(name = "CREATE_TIME", updatable = false)
    private Timestamp createTime;

    @Column(name = "UPDATE_TIME")
    private Timestamp updateTime;

    @Column(name = "CREATE_BY", updatable = false, columnDefinition = "SYSTEM")
    private String createBy;

    @Column(name = "UPDATE_BY")
    private String updateBy;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ACCOUNT_ROLE", joinColumns = @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"))
    private List<Role> roles;

    @OneToOne
    @JoinColumn(name = "MEDICAL_STAFF_ID", nullable = false)
    private MedicalStaff medicalStaff;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Account account = (Account) o;
        return getId() != null && Objects.equals(getId(), account.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
