package com.binhnd.pmsbe.controller;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.common.constants.RequestAction;
import com.binhnd.pmsbe.dto.request.AuthenticationRequest;
import com.binhnd.pmsbe.dto.response.AuthenticationResponse;
import com.binhnd.pmsbe.services.authentication.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PMSConstants.PREFIX_URL)
public class AuthenticationController {

    private final Logger L = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = RequestAction.Authentication.LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        L.info("[POST] {}: login to PMS system", PMSConstants.PREFIX_URL + "/login");
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping(value = RequestAction.Authentication.REFRESH_TOKEN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        L.info("[POST] {}: refresh token", PMSConstants.PREFIX_URL + "/refresh-token");
        return ResponseEntity.ok().body(authenticationService.refreshToken(bearerToken));
    }

}
