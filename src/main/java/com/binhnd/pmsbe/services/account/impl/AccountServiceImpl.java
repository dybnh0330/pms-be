package com.binhnd.pmsbe.services.account.impl;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.AccountResponse;
import com.binhnd.pmsbe.common.enums.Account;
import com.binhnd.pmsbe.mapper.AccountMapper;
import com.binhnd.pmsbe.repositories.AccountRepository;
import com.binhnd.pmsbe.repositories.custom_repositories.CustomAccountRepository;
import com.binhnd.pmsbe.services.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomAccountRepository customAccountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              CustomAccountRepository customAccountRepository) {
        this.accountRepository = accountRepository;
        this.customAccountRepository = customAccountRepository;
    }

    @Override
    public AccountResponse findAccountById(Long id) {

        Optional<Account> account = accountRepository.findById(id);
        if (account.isEmpty()) {
            throw new PMSException(EnumPMSException.ACCOUNT_NOT_EXISTED);
        }

        return AccountMapper.toDto(account.get());
    }

    @Override
    public List<AccountResponse> findAllAccount() {

        List<Account> accounts = accountRepository.findAll();

        return AccountMapper.mapAll(accounts);
    }

    @Override
    public Page<AccountResponse> findAllAccountPage(SearchSortPageableDTO dto) {

        return customAccountRepository.findAllAccountPage(dto);
    }

}
