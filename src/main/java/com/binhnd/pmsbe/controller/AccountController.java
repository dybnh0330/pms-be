package com.binhnd.pmsbe.controller;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.common.constants.RequestAction;
import com.binhnd.pmsbe.dto.request.AccountRequest;
import com.binhnd.pmsbe.dto.request.ChangePasswordRequest;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.AccountResponse;
import com.binhnd.pmsbe.services.account.AccountCUService;
import com.binhnd.pmsbe.services.account.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PMSConstants.PREFIX_URL + RequestAction.ACCOUNT)
public class AccountController {

    private final Logger L = LoggerFactory.getLogger(AccountController.class);

    private final AccountCUService accountCUService;
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountCUService accountCUService, AccountService accountService) {
        this.accountCUService = accountCUService;
        this.accountService = accountService;
    }


    @PostMapping(value = RequestAction.Account.CREATE_ACCOUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) {
        L.info("[POST] {}: create new an account", PMSConstants.PREFIX_URL + "/account/create");
        return ResponseEntity.ok().body(accountCUService.createAccount(request));
    }

    @PutMapping(value = RequestAction.Account.UPDATE_ACCOUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> updateAccount(@RequestParam Long id, @RequestBody AccountRequest request) {
        L.info("[PUT] {}: update an account existed", PMSConstants.PREFIX_URL + "/account/update");
        return ResponseEntity.ok().body(accountCUService.updateAccount(id, request));
    }

    @PutMapping(value = RequestAction.Account.UNLOCK, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> unlockAccount(@RequestParam Long id) {
        L.info("[PUT] {}: unlock an lock-account", PMSConstants.PREFIX_URL + "/account/unlock?id=" + id);
        accountCUService.unlockAccount(id);
        return ResponseEntity.ok().body("\"Unlock account successfully!\"");
    }

    @PutMapping(value = RequestAction.Account.CHANGE_PASSWORD, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        L.info("[PUT] {}: change password", PMSConstants.PREFIX_URL + "/account/change-password");
        accountCUService.changePassword(request);
        return ResponseEntity.ok().body("\"Change password account successfully!\"");
    }

    @PutMapping(value = RequestAction.Account.RESET_PASSWORD, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> resetPassword(@RequestParam Long id) {
        L.info("[PUT] {}: reset password", PMSConstants.PREFIX_URL + "/account/reset-password?id=" + id);
        accountCUService.resetPassword(id);
        return ResponseEntity.ok().body("\"Reset password account successfully!\"");
    }

    @DeleteMapping(value = RequestAction.Account.DELETE_ACCOUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteAccount(@RequestParam Long id) {
        L.info("[DELETE] {}: delete an account", PMSConstants.PREFIX_URL + "/account/delete?id=" + id);
        accountCUService.deleteAccount(id);
        return ResponseEntity.ok().body("\"Delete account successfully!\"");
    }

    @GetMapping(value = RequestAction.Account.FIND_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> findAccountById(@RequestParam Long id) {
        L.info("[GET] {}: find account by id", PMSConstants.PREFIX_URL + "/account/find-by-id?id=" + id);
        return ResponseEntity.ok().body(accountService.findAccountById(id));
    }

    @GetMapping(value = RequestAction.Account.FIND_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AccountResponse>> findAllAccount() {
        L.info("[GET] {}: find all account", PMSConstants.PREFIX_URL + "/account/find-all");
        return ResponseEntity.ok().body(accountService.findAllAccount());
    }

    @GetMapping(value = RequestAction.Account.FIND_ALL_PAGE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AccountResponse>> findAllAccountPage(SearchSortPageableDTO dto) {
        L.info("[GET] {}: find all page account", PMSConstants.PREFIX_URL + "/account/page/find-all");
        return ResponseEntity.ok().body(accountService.findAllAccountPage(dto));
    }
}
