package com.binhnd.pmsbe.services.authentication.impl;

import com.binhnd.pmsbe.common.enums.Account;
import com.binhnd.pmsbe.entity.Department;
import com.binhnd.pmsbe.entity.Role;
import com.binhnd.pmsbe.repositories.AccountRepository;
import com.binhnd.pmsbe.services.authentication.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {

    private final AccountRepository accountRepository;

    @Autowired
    public JwtServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Claims parseToken(String token, PublicKey key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public String createToken(Date issuedAt, int lifeTime, Map<String, Object> claims, PrivateKey key) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(issuedAt);
        calendar.add(Calendar.SECOND, lifeTime);
        Date expiredAt = calendar.getTime();

        return Jwts.builder()
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .addClaims(claims)
                .signWith(key)
                .compact();
    }

    @Override
    public Map<String, Object> getAccountUser(String user) {

        Account account = accountRepository.findAccountByUsername(user);

        String name = account.getMedicalStaff().getName();

        Department departmentClaims = account.getMedicalStaff().getDepartment();

        String username = account.getUsername();

        HashMap<String, Object> tokenClaims = new HashMap<>();

        tokenClaims.put("name", name);
        tokenClaims.put("username", username);

        if (!ObjectUtils.isEmpty(departmentClaims)) {
            String department = departmentClaims.getName();
            Long departmentId = departmentClaims.getId();

            tokenClaims.put("department", department);
            tokenClaims.put("departmentId", departmentId);
        }

        if (!ObjectUtils.isEmpty(account)) {
            List<Role> roles = account.getRoles();
            List<String> scopes = roles.stream().map(Role::getName).collect(Collectors.toList());

            if (!scopes.isEmpty()) {
                tokenClaims.put("scope", scopes);
            }
        }

        return tokenClaims;
    }
}
