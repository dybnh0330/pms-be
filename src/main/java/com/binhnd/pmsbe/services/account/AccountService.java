package com.binhnd.pmsbe.services.account;

import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.AccountResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AccountService {

    AccountResponse findAccountById(Long id);

    List<AccountResponse> findAllAccount();

    Page<AccountResponse> findAllAccountPage(SearchSortPageableDTO dto);

}
