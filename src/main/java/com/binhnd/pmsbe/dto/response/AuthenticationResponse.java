package com.binhnd.pmsbe.dto.response;

import lombok.Data;

@Data
public class AuthenticationResponse {

    private String access_token;
    private long expires_in;
    private String[] scopes;
}
