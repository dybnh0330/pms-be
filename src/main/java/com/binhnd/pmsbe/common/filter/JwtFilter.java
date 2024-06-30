package com.binhnd.pmsbe.common.filter;

import com.binhnd.pmsbe.common.config.TokenAuthentication;
import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.common.utils.jwt.SecurityUtils;
import com.binhnd.pmsbe.services.authentication.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtService jwtService;

    @Autowired
    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String url = request.getRequestURI();

        if (!ObjectUtils.isEmpty(authHeader) && authHeader.startsWith(PMSConstants.JWT_PREFIX)
                && !url.equals(PMSConstants.PREFIX_URL + "/login") ) {
            String token = authHeader.split(PMSConstants.JWT_PREFIX)[1].trim();

            try {
                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(SecurityUtils.getPublicKey("publickey.pem"))
                        .build()
                        .parseClaimsJws(token);

                Claims body = claimsJws.getBody();

                ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();

                List<String> scope = (List<String>) body.get("scope");
                for (String s : scope) {
                    authorities.add(new SimpleGrantedAuthority(s));
                }

                TokenAuthentication authentication = new TokenAuthentication(body, body, authorities);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                LOGGER.error("Token is not valid");
            }
        }

        filterChain.doFilter(request, response);

    }
}
