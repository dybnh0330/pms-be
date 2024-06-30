package com.binhnd.pmsbe.repositories.custom_repositories;

import com.binhnd.pmsbe.common.constants.MedicalStaffConstant;
import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.AccountResponse;
import com.binhnd.pmsbe.common.enums.Account;
import com.binhnd.pmsbe.mapper.AccountMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.List;

import static com.binhnd.pmsbe.common.constants.AccountConstant.*;
import static com.binhnd.pmsbe.common.constants.PMSConstants.ASC;

@Repository
public class CustomAccountRepository {

    @PersistenceContext
    private EntityManager em;

    public Page<AccountResponse> findAllAccountPage(SearchSortPageableDTO dto) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Account> query = cb.createQuery(Account.class);
        Root<Account> root = query.from(Account.class);

        Predicate username = cb.like(root.get(USERNAME), "%" + dto.getSearchText() + "%");
        Predicate staffName = cb.like(root.get(MEDICAL_STAFF).get(MedicalStaffConstant.NAME), "%" + dto.getSearchText() + "%");
        Predicate createBy = cb.like(root.get(CREATE_BY), "%" + dto.getSearchText() + "%");

        Predicate searchAccount = cb.or(username, staffName, createBy);

        if (!ObjectUtils.isEmpty(dto.getSortColumn()) && !ObjectUtils.isEmpty(dto.getSortDirection())) {
            if (StringUtil.uppercaseAndRemoveWhitespace(dto.getSortDirection()).equals(ASC.toUpperCase())) {
                query.orderBy(cb.asc(root.get(dto.getSortColumn())));
            } else {
                query.orderBy(cb.desc(root.get(dto.getSortColumn())));
            }
        }

        Pageable pageable = PageRequest.of(dto.getCurrentPageNumber(), dto.getPageSize());

        query.where(searchAccount);

        TypedQuery<Account> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Account> accounts = typedQuery.getResultList();

        List<AccountResponse> accountResponses = AccountMapper.mapAll(accounts);

        CriteriaQuery<Long> countAccount = cb.createQuery(Long.class);
        countAccount.select(cb.count(countAccount.from(Account.class)));
        countAccount.where(searchAccount);

        TypedQuery<Long> typedAccount = em.createQuery(countAccount);
        long totalAccount = typedAccount.getSingleResult();

        return new PageImpl<>(accountResponses, pageable, totalAccount);
    }

    public Long countAccount() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Long> countAccount = cb.createQuery(Long.class);
        countAccount.select(cb.count(countAccount.from(Account.class)));

        TypedQuery<Long> countTypedQuery = em.createQuery(countAccount);

        return countTypedQuery.getSingleResult();
    }

}
