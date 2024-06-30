package com.binhnd.pmsbe.services.authentication;

import com.binhnd.pmsbe.dto.request.AuthenticationRequest;
import com.binhnd.pmsbe.dto.response.AuthenticationResponse;

import java.io.IOException;

public interface AuthenticationService {

    AuthenticationResponse login(AuthenticationRequest request);

    AuthenticationResponse token(String bearerToken);

    AuthenticationResponse refreshToken(String bearerToken);

}
