package com.binhnd.pmsbe;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.common.constants.RoleName;
import com.binhnd.pmsbe.common.enums.Account;
import com.binhnd.pmsbe.entity.MedicalStaff;
import com.binhnd.pmsbe.entity.Role;
import com.binhnd.pmsbe.repositories.AccountRepository;
import com.binhnd.pmsbe.repositories.MedicalStaffRepository;
import com.binhnd.pmsbe.repositories.RoleRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeding implements CommandLineRunner {

    private final Logger L = LoggerFactory.getLogger(DataSeeding.class);

    private final MedicalStaffRepository medicalStaffRepository;

    private final AccountRepository accountRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataSeeding(MedicalStaffRepository medicalStaffRepository,
                       AccountRepository accountRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.medicalStaffRepository = medicalStaffRepository;
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        L.info("Start seed data");
        try {
            seedData();
        } catch (Exception e) {
            L.error(String.format("Cannot seed unit data, error : %s", e.getMessage()));
        }

        L.info("End seed data");
    }

    private void seedData() {
        List<MedicalStaff> staffs = medicalStaffRepository.findAll();

        List<Account> accounts = accountRepository.findAll();

        List<Role> allRole = roleRepository.findAll();

        if (allRole.isEmpty()) {
            for (String roleName : RoleName.PMS_ROLE) {
                Role role = new Role();
                role.setName(roleName);
                role.setCreateTime(Timestamp.from(Instant.now()));
                role.setCreateBy("SYSTEM");
                roleRepository.save(role);
            }
        }


        if (staffs.isEmpty() && accounts.isEmpty() && !allRole.isEmpty()) {
            L.info("Start create account admin");

            MedicalStaff medicalStaff = new MedicalStaff();
            medicalStaff.setName("Quản trị viên hệ thống");
            medicalStaff.setGender(0L);
            medicalStaff.setDob(Timestamp.valueOf("1999-01-01 00:00:00"));
            medicalStaff.setAddress("Hà Nội");
            medicalStaff.setCccd("00110203041");
            medicalStaff.setPhoneNumber("0386270801");
            medicalStaff.setEmail("admin-pms@gmail.com");
            medicalStaff.setCreateTime(Timestamp.from(Instant.now()));
            medicalStaff.setCreateBy("SYSTEM");

            MedicalStaff admin = medicalStaffRepository.save(medicalStaff);

            Role roleAdmin = roleRepository.findRoleByName(RoleName.ROLE_ADMIN);


            List<Role> roles = new ArrayList<>();
            if (!ObjectUtils.isEmpty(roleAdmin)) {
                roles.add(roleAdmin);
            }

            Account account = new Account();
            account.setMedicalStaff(admin);

            if (!roles.isEmpty() || !ObjectUtils.isEmpty(roles)) {
                account.setRoles(roles);
            }
            account.setUsername("admin");
            account.setPassword(passwordEncoder.encode(PMSConstants.DEFAULT_PASSWORD));
            account.setCreateTime(Timestamp.from(Instant.now()));
            account.setCreateBy("SYSTEM");
            account.setStatus(true);


            accountRepository.save(account);

        }
    }
}
