package com.binhnd.pmsbe.repositories.custom_repositories;

import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.dto.request.CategoryRequest;
import com.binhnd.pmsbe.dto.request.SearchSortDto;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.CategoryResponse;
import com.binhnd.pmsbe.entity.Category;
import com.binhnd.pmsbe.mapper.CategoryMapper;
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

import static com.binhnd.pmsbe.common.constants.CategoryConstant.*;
import static com.binhnd.pmsbe.common.constants.PMSConstants.ASC;

@Repository
public class CustomCategoryRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Category> findCategoryByParentId(Long parentId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Category> query = cb.createQuery(Category.class);
        Root<Category> root = query.from(Category.class);

        Predicate predicate = cb.equal(root.get(PARENT_ID).get(ID), parentId);

        CriteriaQuery<Category> findCategory = query.where(predicate);

        TypedQuery<Category> typedQuery = em.createQuery(findCategory);

        return typedQuery.getResultList();
    }

    public Category findExistCategory(CategoryRequest request) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Category> query = cb.createQuery(Category.class);
        Root<Category> root = query.from(Category.class);

        Predicate categoryName = cb.equal(root.get(NAME), request.getName());
        Predicate categoryType = cb.equal(root.get(PARENT_ID).get(ID), request.getParentId());

        CriteriaQuery<Category> findCategory = query.where(categoryName, categoryType);

        TypedQuery<Category> typedQuery = em.createQuery(findCategory);

        List<Category> categories = typedQuery.getResultList();

        if (categories.isEmpty()) {
            return null;
        }

        return categories.get(0);
    }

    public List<Category> findAllParentCategory(SearchSortDto dto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Category> query = cb.createQuery(Category.class);
        Root<Category> root = query.from(Category.class);

        Predicate parentCategory = cb.isNull(root.get(PARENT_ID));

        Predicate searchCategory = cb.or(
                cb.like(root.get(NAME), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(DESCRIPTION), "%" + dto.getSearchText() + "%"));

        CriteriaQuery<Category> findCategory = query.where(parentCategory, searchCategory);

        if (!ObjectUtils.isEmpty(dto.getSortColumn()) && !ObjectUtils.isEmpty(dto.getSortDirection())) {
            if (StringUtil.uppercaseAndRemoveWhitespace(dto.getSortDirection()).equals(ASC.toUpperCase())) {
                query.orderBy(cb.asc(root.get(dto.getSortColumn())));
            } else {
                query.orderBy(cb.desc(root.get(dto.getSortColumn())));
            }
        }

        TypedQuery<Category> typedQuery = em.createQuery(findCategory);

        return typedQuery.getResultList();
    }

    public Page<CategoryResponse> findAllPageCategoryByParentId(Long parentId, SearchSortPageableDTO dto) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Category> query = cb.createQuery(Category.class);
        Root<Category> root = query.from(Category.class);

        Predicate categoryByParentId = cb.equal(root.get(PARENT_ID).get(ID), parentId);

            Predicate searchCategory = cb.or(
                    cb.like(root.get(NAME), "%" + dto.getSearchText() + "%"),
                    cb.like(root.get(PARENT_ID).get(NAME), "%" + dto.getSearchText() + "%"),
                    cb.like(root.get(DESCRIPTION), "%" + dto.getSearchText() + "%"));

            Predicate categoryWithTextSearch = cb.and(categoryByParentId, searchCategory);

        if (!ObjectUtils.isEmpty(dto.getSortColumn()) && !ObjectUtils.isEmpty(dto.getSortDirection())) {
            if (StringUtil.uppercaseAndRemoveWhitespace(dto.getSortDirection()).equals(ASC.toUpperCase())) {
                query.orderBy(cb.asc(root.get(dto.getSortColumn())));
            } else {
                query.orderBy(cb.desc(root.get(dto.getSortColumn())));
            }
        }

        Pageable pageable = PageRequest.of(dto.getCurrentPageNumber(), dto.getPageSize());

        query.where(categoryWithTextSearch);
        TypedQuery<Category> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Category> categories = typedQuery.getResultList();

        List<CategoryResponse> responses = CategoryMapper.mapAll(categories);

        CriteriaQuery<Long> countCategory = cb.createQuery(Long.class);
        countCategory.select(cb.count(countCategory.from(Category.class)));
        countCategory.where(categoryWithTextSearch, categoryByParentId);

        TypedQuery<Long> typedCountCategory = em.createQuery(countCategory);
        long totalDepartment = typedCountCategory.getSingleResult();

        return new PageImpl<>(responses, pageable, totalDepartment);
    }

    public Page<CategoryResponse> findAllCategoryPage(SearchSortPageableDTO dto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Category> query = cb.createQuery(Category.class);
        Root<Category> root = query.from(Category.class);

        Predicate categoryTypeNotNull = cb.isNotNull(root.get(PARENT_ID).get(ID));

        Predicate searchCategory = cb.or(
                cb.like(root.get(NAME), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(PARENT_ID).get(NAME), "%" + dto.getSearchText() + "%"),
                cb.like(root.get(DESCRIPTION), "%" + dto.getSearchText() + "%"));

        Predicate categoryWithTextSearch = cb.and(searchCategory, categoryTypeNotNull);

        if (!ObjectUtils.isEmpty(dto.getSortColumn()) && !ObjectUtils.isEmpty(dto.getSortDirection())) {
            if (StringUtil.uppercaseAndRemoveWhitespace(dto.getSortDirection()).equals(ASC.toUpperCase())) {
                query.orderBy(cb.asc(root.get(dto.getSortColumn())));
            } else {
                query.orderBy(cb.desc(root.get(dto.getSortColumn())));
            }
        }

        Pageable pageable = PageRequest.of(dto.getCurrentPageNumber(), dto.getPageSize());
        query.where(categoryWithTextSearch);
        TypedQuery<Category> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Category> categories = typedQuery.getResultList();

        List<CategoryResponse> responses = CategoryMapper.mapAll(categories);

        CriteriaQuery<Long> countCategory = cb.createQuery(Long.class);
        countCategory.select(cb.count(countCategory.from(Category.class)));
        countCategory.where(categoryWithTextSearch, categoryTypeNotNull);

        TypedQuery<Long> typedCountCategory = em.createQuery(countCategory);
        long totalDepartment = typedCountCategory.getSingleResult();

        return new PageImpl<>(responses, pageable, totalDepartment);
    }
}
