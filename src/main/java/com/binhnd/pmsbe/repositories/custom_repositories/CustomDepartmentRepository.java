package com.binhnd.pmsbe.repositories.custom_repositories;

import com.binhnd.pmsbe.common.utils.ObjectMapperUtils;
import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.dto.request.DepartmentRequest;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.DepartmentResponse;
import com.binhnd.pmsbe.entity.Department;
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

import static com.binhnd.pmsbe.common.constants.DepartmentConstant.NAME;
import static com.binhnd.pmsbe.common.constants.PMSConstants.ASC;

@Repository
public class CustomDepartmentRepository {

    @PersistenceContext
    private EntityManager em;

    public Page<DepartmentResponse> findAllDepartment(SearchSortPageableDTO dto) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Department> query = cb.createQuery(Department.class);
        Root<Department> root = query.from(Department.class);

        Predicate searchDepartment = cb.like(root.get("name"), "%" + dto.getSearchText() + "%");

        if (!ObjectUtils.isEmpty(dto.getSortColumn()) && !ObjectUtils.isEmpty(dto.getSortDirection())) {
            if (StringUtil.uppercaseAndRemoveWhitespace(dto.getSortDirection()).equals(ASC.toUpperCase())) {
                query.orderBy(cb.asc(root.get(dto.getSortColumn())));
            } else {
                query.orderBy(cb.desc(root.get(dto.getSortColumn())));
            }
        }

        Pageable pageable = PageRequest.of(dto.getCurrentPageNumber(), dto.getPageSize());

        query.where(searchDepartment);
        TypedQuery<Department> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Department> departments = typedQuery.getResultList();

        List<DepartmentResponse> departmentResponses = ObjectMapperUtils.mapAll(departments, DepartmentResponse.class);

        CriteriaQuery<Long> countDepartment = cb.createQuery(Long.class);
        countDepartment.select(cb.count(countDepartment.from(Department.class)));
        countDepartment.where(searchDepartment);

        TypedQuery<Long> typedCountDepartment = em.createQuery(countDepartment);
        long totalDepartment = typedCountDepartment.getSingleResult();

        return new PageImpl<>(departmentResponses, pageable, totalDepartment);
    }

    public Department findExistDepartment(DepartmentRequest department) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Department> query = cb.createQuery(Department.class);
        Root<Department> root = query.from(Department.class);

        Predicate departmentExisted = cb.equal(root.get(NAME), department.getName());

        CriteriaQuery<Department> findDepartment = query.where(departmentExisted);

        TypedQuery<Department> typeFindDepartment = em.createQuery(findDepartment);

        List<Department> departments = typeFindDepartment.getResultList();

        if (departments.isEmpty()) {
            return null;
        }

        return departments.get(0);
    }

    public Long countDepartment() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Long> countDepartment = cb.createQuery(Long.class);
        countDepartment.select(cb.count(countDepartment.from(Department.class)));

        TypedQuery<Long> countTypedQuery = em.createQuery(countDepartment);

        return countTypedQuery.getSingleResult();
    }
}
