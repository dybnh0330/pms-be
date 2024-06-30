package com.binhnd.pmsbe.services.dashboard;

import com.binhnd.pmsbe.dto.response.DashboardResponse;
import com.binhnd.pmsbe.dto.response.StaffPerDepartment;

public interface DashboardService {
    DashboardResponse findInformation();

    StaffPerDepartment findStaffPerDepartment();
}
