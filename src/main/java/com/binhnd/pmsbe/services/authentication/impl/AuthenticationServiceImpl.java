package com.binhnd.pmsbe.services.authentication.impl;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.common.utils.jwt.SecurityUtils;
import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.dto.request.AuthenticationRequest;
import com.binhnd.pmsbe.dto.response.AuthenticationResponse;
import com.binhnd.pmsbe.common.enums.Account;
import com.binhnd.pmsbe.repositories.AccountRepository;
import com.binhnd.pmsbe.services.authentication.AuthenticationService;
import com.binhnd.pmsbe.services.authentication.JwtService;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.binhnd.pmsbe.common.constants.PMSConstants.*;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;

    @Value("${pms.token.lifetime}")
    private Integer tokenLifeTime;

    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                     AccountRepository accountRepository,
                                     JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
        this.jwtService = jwtService;
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {

        validateAndCorrectDataLogin(request);

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            Account accountByUsername = accountRepository.findAccountByUsername(authentication.getName());

            Date issueAt = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(issueAt);
            calendar.add(Calendar.SECOND, tokenLifeTime);

            if (ObjectUtils.isEmpty(accountByUsername)) {
                throw new PMSException(EnumPMSException.ACCOUNT_NOT_EXISTED);
            }

            if (!accountByUsername.getStatus()) {
                throw new PMSException(EnumPMSException.ACCOUNT_LOCK_NOT_LOGIN);
            }

            Map<String, Object> tokenClaims = jwtService.getAccountUser(accountByUsername.getUsername());

            if (ObjectUtils.isEmpty(tokenClaims)) {
                throw new PMSException(EnumPMSException.UNAUTHORIZED);
            }

            List<String> scopes = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

            AuthenticationResponse response = new AuthenticationResponse();

            String token = jwtService.createToken(issueAt, tokenLifeTime, tokenClaims, SecurityUtils.getPrivateKey(APP_PRIVATE_KEY_FILE));

            response.setAccess_token(token);
            response.setExpires_in(tokenLifeTime);
            response.setScopes(scopes.toArray(new String[]{}));

            return response;

        } catch (BadCredentialsException exception) {
            throw new PMSException(EnumPMSException.USERNAME_OR_PASSWORD_INCORRECT);
        } catch (IOException e) {
            throw new PMSException(EnumPMSException.LOGIN_FAIL);
        }

    }

    @Override
    public AuthenticationResponse token(String bearerToken) {

        if (ObjectUtils.isEmpty(bearerToken) || !bearerToken.contains(JWT_PREFIX)) {
            throw new PMSException(EnumPMSException.TOKEN_INVALID);
        }

        return null;
    }

    @Override
    public AuthenticationResponse refreshToken(String bearerToken) {

        if (ObjectUtils.isEmpty(bearerToken) || !bearerToken.contains(JWT_PREFIX)) {
            throw new PMSException(EnumPMSException.TOKEN_INVALID);
        }

        bearerToken = bearerToken.split(JWT_PREFIX)[1];

        try {

            Claims claims = jwtService.parseToken(bearerToken, SecurityUtils.getPublicKey("publickey.pem"));

            if (ObjectUtils.isEmpty(claims) || !claims.containsKey(APP_USERNAME_TOKEN_FIELD)) {
                throw new PMSException(EnumPMSException.TOKEN_INVALID);
            }

            String username = claims.get(APP_USERNAME_TOKEN_FIELD, String.class);

            AuthenticationResponse response = new AuthenticationResponse();

            Map<String, Object> tokenClaims = jwtService.getAccountUser(username);

            if (tokenClaims.isEmpty()) {
                throw new PMSException(EnumPMSException.TOKEN_INVALID);
            }

            Date issueAt = new Date();

            String token = jwtService.createToken(issueAt, tokenLifeTime, tokenClaims, SecurityUtils.getPrivateKey(APP_PRIVATE_KEY_FILE));

            response.setAccess_token(token);
            response.setExpires_in(tokenLifeTime);

            Object scope = tokenClaims.get(APP_SCOPE_TOKEN_FIELD);

            if (scope instanceof List<?>) {
                response.setScopes(((List<?>) scope).toArray(new String[]{}));
            }

            return response;

        } catch (Exception e) {
            throw new PMSException(EnumPMSException.TOKEN_INVALID);
        }
    }

    private void validateAndCorrectDataLogin(AuthenticationRequest request) {
        if (ObjectUtils.isEmpty(request)
                || ObjectUtils.isEmpty(request.getPassword())
                || ObjectUtils.isEmpty(request.getUsername())) {
            throw new PMSException(EnumPMSException.AUTHENTICATION_DATA_INVALID);
        }

        request.setUsername(StringUtil.removeWhitespace(request.getUsername()));
        request.setPassword(StringUtil.removeWhitespace(request.getPassword()));
    }

}
