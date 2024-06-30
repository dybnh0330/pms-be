package com.binhnd.pmsbe.services.role;

import com.binhnd.pmsbe.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {

    List<RoleResponse> findAllRole();

}
