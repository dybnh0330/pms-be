package com.binhnd.pmsbe.controller;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.dto.response.DashboardResponse;
import com.binhnd.pmsbe.services.dashboard.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = PMSConstants.PREFIX_URL + "/dashboard")
public class DashboardController {

    private final Logger L = LoggerFactory.getLogger(DashboardController.class);

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping(value = "/find-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DashboardResponse> findInformation() {
        L.info("[GET]{}: dashboard information of hospital", PMSConstants.PREFIX_URL + "/dashboard/find-info");
        return ResponseEntity.ok().body(dashboardService.findInformation());
    }

}
