package com.binhnd.pmsbe.services.account;

import com.binhnd.pmsbe.dto.request.AccountRequest;
import com.binhnd.pmsbe.dto.request.ChangePasswordRequest;
import com.binhnd.pmsbe.dto.response.AccountResponse;

public interface AccountCUService {

    AccountResponse createAccount(AccountRequest request);

    AccountResponse updateAccount(Long id, AccountRequest request);

    void unlockAccount(Long id);

    void changePassword(ChangePasswordRequest request);

    void deleteAccount(Long id);

    void resetPassword(Long id);
}
