package com.binhnd.pmsbe.services.role.impl;

import com.binhnd.pmsbe.common.utils.ObjectMapperUtils;
import com.binhnd.pmsbe.dto.response.RoleResponse;
import com.binhnd.pmsbe.entity.Role;
import com.binhnd.pmsbe.repositories.RoleRepository;
import com.binhnd.pmsbe.services.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleResponse> findAllRole() {

        List<Role> roles = roleRepository.findAll();

        return ObjectMapperUtils.mapAll(roles, RoleResponse.class);
    }
}
