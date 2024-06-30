package com.binhnd.pmsbe.services.authentication;

import io.jsonwebtoken.Claims;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

public interface JwtService {

    Claims parseToken(String token, PublicKey key);

    String createToken(Date issuedAt, int lifeTime, Map<String, Object> claims, PrivateKey key);

    Map<String, Object> getAccountUser(String username);
}
