package com.binhnd.pmsbe.services.authentication.impl;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.dto.UserDetail;
import com.binhnd.pmsbe.common.enums.Account;
import com.binhnd.pmsbe.repositories.AccountRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationUserService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Autowired
    public AuthenticationUserService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountRepository.findAccountByUsername(username);
        if (ObjectUtils.isEmpty(account)) {
            throw new PMSException(EnumPMSException.ACCOUNT_NOT_EXISTED);
        }

        return new UserDetail(account);
    }
}
