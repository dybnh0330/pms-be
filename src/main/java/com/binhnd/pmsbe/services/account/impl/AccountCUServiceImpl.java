package com.binhnd.pmsbe.services.account.impl;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.common.constants.RoleName;
import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.common.utils.jwt.SecurityUtils;
import com.binhnd.pmsbe.dto.request.AccountRequest;
import com.binhnd.pmsbe.dto.request.ChangePasswordRequest;
import com.binhnd.pmsbe.dto.response.AccountResponse;
import com.binhnd.pmsbe.common.enums.Account;
import com.binhnd.pmsbe.entity.MedicalStaff;
import com.binhnd.pmsbe.entity.Role;
import com.binhnd.pmsbe.mapper.AccountMapper;
import com.binhnd.pmsbe.repositories.AccountRepository;
import com.binhnd.pmsbe.repositories.MedicalStaffRepository;
import com.binhnd.pmsbe.repositories.RoleRepository;
import com.binhnd.pmsbe.services.account.AccountCUService;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.binhnd.pmsbe.common.constants.PMSConstants.DEFAULT_PASSWORD;
import static com.binhnd.pmsbe.common.constants.PMSConstants.UNLOCK_ACCOUNT;

@Service
public class AccountCUServiceImpl implements AccountCUService {

    private final Logger L = LoggerFactory.getLogger(AccountCUServiceImpl.class);

    private final AccountRepository accountRepository;
    private final MedicalStaffRepository medicalStaffRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AccountCUServiceImpl(AccountRepository accountRepository,
                                MedicalStaffRepository medicalStaffRepository,
                                RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.medicalStaffRepository = medicalStaffRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AccountResponse createAccount(AccountRequest request) {

        validateAndCorrectData(request);
        checkDuplicateUser(request, null);

        String username = SecurityUtils.getCurrentUsername();

        Optional<MedicalStaff> optionalMedicalStaff = medicalStaffRepository.findById(request.getMedicalStaffId());

        List<Long> roleIds = request.getRoleIds();

        List<Role> roles = new ArrayList<>();

        List<String> roleNames = new ArrayList<>();

        for (Long id : roleIds) {
            Optional<Role> optionalRole = roleRepository.findById(id);
            if (optionalRole.isEmpty()) {
                throw new PMSException(EnumPMSException.ROLE_NOT_EXISTED);
            }
            Role role = optionalRole.get();
            roles.add(role);
            roleNames.add(role.getName());
        }

        if (optionalMedicalStaff.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_STAFF_NOT_EXISTED);
        }

        MedicalStaff medicalStaff = optionalMedicalStaff.get();

        checkRoleMedicalStaff(medicalStaff, roleNames);

        Account newAccount = new Account();
        newAccount.setUsername(request.getUsername());
        newAccount.setPassword(passwordEncoder.encode(PMSConstants.DEFAULT_PASSWORD));
        newAccount.setRoles(roles);
        newAccount.setMedicalStaff(medicalStaff);
        newAccount.setStatus(request.getStatus());
        newAccount.setCreateTime(Timestamp.from(Instant.now()));
        newAccount.setCreateBy(username);

        accountRepository.save(newAccount);

        return AccountMapper.toDto(newAccount);
    }

    private void checkRoleMedicalStaff(MedicalStaff medicalStaff, List<String> roleNames) {
        if (medicalStaff.getSpecialize().equals("Nhân viên hành chính")
                && (roleNames.contains(RoleName.ROLE_ADMIN)
                || roleNames.contains(RoleName.ROLE_NURSE)
                || roleNames.contains(RoleName.ROLE_EXAM_DOCTOR)
                || roleNames.contains(RoleName.ROLE_SPECIALIST))) {
            throw new PMSException(EnumPMSException.NOT_PERMISSION);
        }

        if ((medicalStaff.getSpecialize().equals("Điều dưỡng") || medicalStaff.getSpecialize().equals("Y tá"))
                && (roleNames.contains(RoleName.ROLE_ADMIN)
                || roleNames.contains(RoleName.ROLE_SPECIALIST)
                || roleNames.contains(RoleName.ROLE_EXAM_DOCTOR))) {
            throw new PMSException(EnumPMSException.NOT_PERMISSION);
        }

        if (medicalStaff.getSpecialize().equals("Bác sĩ")
                && (roleNames.contains(RoleName.ROLE_NURSE)
                || roleNames.contains(RoleName.ROLE_MEDICAL_STAFF))) {
            throw new PMSException(EnumPMSException.NOT_PERMISSION);
        }

        if (medicalStaff.getSpecialize().equals("Bác sĩ")
                && medicalStaff.getDepartment().getName().equalsIgnoreCase("Khoa Khám bệnh")
                && (roleNames.contains(RoleName.ROLE_NURSE) || roleNames.contains(RoleName.ROLE_MEDICAL_STAFF)
                || roleNames.contains(RoleName.ROLE_SPECIALIST))) {
            throw new PMSException(EnumPMSException.NOT_PERMISSION);
        }
    }

    @Override
    public void unlockAccount(Long id) {

        Optional<Account> account = accountRepository.findById(id);

        if (account.isEmpty()) {
            throw new PMSException(EnumPMSException.ACCOUNT_NOT_EXISTED);
        }

        if (account.get().getStatus().equals(Boolean.TRUE)) {
            throw new PMSException(EnumPMSException.ACCOUNT_NOT_LOCK);
        }

        Account unlockAccount = account.get();
        unlockAccount.setUpdateBy(SecurityUtils.getCurrentUsername());
        unlockAccount.setStatus(UNLOCK_ACCOUNT);

        accountRepository.save(unlockAccount);

    }

    @Override
    public void changePassword(ChangePasswordRequest request) {

        validateAndCorrectData(request);

        Account changePasswordAccount = accountRepository.findAccountByUsername(request.getUsername());

        if (ObjectUtils.isEmpty(changePasswordAccount)) {
            throw new PMSException(EnumPMSException.ACCOUNT_NOT_EXISTED);
        }

        if (!changePasswordAccount.getUsername().equalsIgnoreCase(request.getUsername())) {
            throw new PMSException(EnumPMSException.USERNAME_INVALID);
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PMSException(EnumPMSException.PASSWORD_NOT_MATCH);
        }

        if (!passwordEncoder.matches(request.getOldPassword(), changePasswordAccount.getPassword())) {
            throw new PMSException(EnumPMSException.PASSWORD_INCORRECT);
        }

        if (passwordEncoder.matches(request.getNewPassword(), changePasswordAccount.getPassword())) {
            throw new PMSException(EnumPMSException.PASSWORD_INVALID);
        }

        changePasswordAccount.setPassword(passwordEncoder.encode(request.getNewPassword()));
        changePasswordAccount.setUpdateBy(SecurityUtils.getCurrentUsername());

        accountRepository.save(changePasswordAccount);

    }

    @Override
    public AccountResponse updateAccount(Long id, AccountRequest request) {

        String username = SecurityUtils.getCurrentUsername();

        validateAndCorrectData(request);

        checkDuplicateUser(request, id);

        Optional<Account> account = accountRepository.findById(id);

        List<Role> roles = new ArrayList<>();
        List<String> roleNames = new ArrayList<>();

        for (Long roleId : request.getRoleIds()) {
            Optional<Role> optionalRole = roleRepository.findById(roleId);
            if (optionalRole.isEmpty()) {
                throw new PMSException(EnumPMSException.ROLE_NOT_EXISTED);
            }

            Role role = optionalRole.get();
            roles.add(role);
            roleNames.add(role.getName());
        }

        Optional<MedicalStaff> optionalMedicalStaff = medicalStaffRepository.findById(request.getMedicalStaffId());

        if (account.isEmpty()) {
            throw new PMSException(EnumPMSException.ACCOUNT_NOT_EXISTED);
        }


        if (optionalMedicalStaff.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_STAFF_NOT_EXISTED);
        }

        MedicalStaff medicalStaff = optionalMedicalStaff.get();

        checkRoleMedicalStaff(medicalStaff, roleNames);

        Account updateAccount = account.get();
        updateAccount.setUsername(request.getUsername());
        updateAccount.setMedicalStaff(medicalStaff);
        updateAccount.setRoles(roles);
        updateAccount.setStatus(request.getStatus());
        updateAccount.setUpdateTime(Timestamp.from(Instant.now()));
        updateAccount.setUpdateBy(username);

        accountRepository.save(updateAccount);

        return AccountMapper.toDto(updateAccount);
    }

    @Override
    public void deleteAccount(Long id) {

        Optional<Account> account = accountRepository.findById(id);
        if (account.isEmpty()) {
            throw new PMSException(EnumPMSException.ACCOUNT_NOT_EXISTED);
        }

        accountRepository.delete(account.get());
    }

    @Override
    public void resetPassword(Long id) {

        Optional<Account> account = accountRepository.findById(id);
        if (account.isEmpty()) {
            throw new PMSException(EnumPMSException.ACCOUNT_NOT_EXISTED);
        }

        Account resetPassword = account.get();
        resetPassword.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        resetPassword.setUpdateBy(SecurityUtils.getCurrentUsername());

        accountRepository.save(resetPassword);
    }

    private void validateAndCorrectData(AccountRequest request) {
        if (ObjectUtils.isEmpty(request)
                || ObjectUtils.isEmpty(request.getStatus())
                || ObjectUtils.isEmpty(request.getRoleIds())
                || ObjectUtils.isEmpty(request.getMedicalStaffId())
                || ObjectUtils.isEmpty(request.getUsername())) {
            throw new PMSException(EnumPMSException.ACCOUNT_DATA_INVALID);
        }

        request.setUsername(StringUtil.removeWhitespace(request.getUsername()));
    }

    private void validateAndCorrectData(ChangePasswordRequest request) {
        if (ObjectUtils.isEmpty(request)
                || ObjectUtils.isEmpty(request.getOldPassword())
                || ObjectUtils.isEmpty(request.getNewPassword())
                || ObjectUtils.isEmpty(request.getConfirmPassword())
                || ObjectUtils.isEmpty(request.getUsername())) {
            throw new PMSException(EnumPMSException.ACCOUNT_DATA_INVALID);
        }
        request.setUsername(StringUtil.removeWhitespace(request.getUsername()));
        request.setOldPassword(StringUtil.removeWhitespace(request.getOldPassword()));
        request.setNewPassword(StringUtil.removeWhitespace(request.getNewPassword()));
        request.setConfirmPassword(StringUtil.removeWhitespace(request.getConfirmPassword()));
    }

    private void checkDuplicateUser(AccountRequest request, Long id) {

        List<MedicalStaff> medicalStaffByAccountIsNull = medicalStaffRepository.findMedicalStaffByAccountIsNull();

        Optional<MedicalStaff> medicalStaffRequest = medicalStaffRepository.findById(request.getMedicalStaffId());

        Account accountByUsername = accountRepository.findAccountByUsername(request.getUsername());

        if (medicalStaffRequest.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_STAFF_NOT_EXISTED);
        }

        MedicalStaff medicalStaff = medicalStaffRequest.get();

        if (!ObjectUtils.isEmpty(accountByUsername) && (ObjectUtils.isEmpty(id) || !accountByUsername.getId().equals(id))) {
            throw new PMSException(EnumPMSException.USERNAME_EXISTED);
        }

        if (!medicalStaffByAccountIsNull.contains(medicalStaff) && id == null) {
            throw new PMSException(EnumPMSException.MEDICAL_STAFF_HAD_ACCOUNT);
        }

        if (!medicalStaffByAccountIsNull.contains(medicalStaff) && !medicalStaff.getAccount().getId().equals(id)) {
            throw new PMSException(EnumPMSException.MEDICAL_STAFF_HAD_ACCOUNT);
        }

    }
}
