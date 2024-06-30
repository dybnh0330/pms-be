package com.binhnd.pmsbe.mapper;

import com.binhnd.pmsbe.dto.response.AccountResponse;
import com.binhnd.pmsbe.common.enums.Account;
import com.binhnd.pmsbe.entity.Role;

import java.util.ArrayList;
import java.util.List;

public class AccountMapper {

    private AccountMapper() {}

    public static AccountResponse toDto (Account entity) {

        AccountResponse dto = new AccountResponse();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());

        List<Role> roles = entity.getRoles();
        List<Long> roleIds = new ArrayList<>();
        List<String> roleNames = new ArrayList<>();
        for (Role role : roles) {
            roleIds.add(role.getId());
            roleNames.add(role.getName());
        }

        dto.setRoleIds(roleIds);
        dto.setRoleNames(roleNames);
        dto.setStatus(entity.getStatus());
        dto.setMedicalStaffId(entity.getMedicalStaff().getId());
        dto.setMedicalStaffName(entity.getMedicalStaff().getName());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        dto.setCreateBy(entity.getCreateBy());
        dto.setUpdateBy(entity.getUpdateBy());

        return dto;
    }

    public static List<AccountResponse> mapAll(List<Account> entities) {

        List<AccountResponse> responses = new ArrayList<>();
        for (Account account: entities) {
            responses.add(toDto(account));
        }

        return responses;
    }
}
