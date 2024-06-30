package com.binhnd.pmsbe.services.category.impl;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.common.utils.ObjectMapperUtils;
import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.common.utils.jwt.SecurityUtils;
import com.binhnd.pmsbe.dto.request.CategoryRequest;
import com.binhnd.pmsbe.dto.response.CategoryResponse;
import com.binhnd.pmsbe.entity.Category;
import com.binhnd.pmsbe.mapper.CategoryMapper;
import com.binhnd.pmsbe.repositories.CategoryRepository;
import com.binhnd.pmsbe.repositories.custom_repositories.CustomCategoryRepository;
import com.binhnd.pmsbe.services.category.CategoryCUService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.binhnd.pmsbe.common.constants.CategoryConstant.PARENT_CATEGORY;

@Service
public class CategoryCUServiceImpl implements CategoryCUService {

    private final CategoryRepository categoryRepository;
    private final CustomCategoryRepository customCategoryRepository;

    @Autowired
    public CategoryCUServiceImpl(CategoryRepository categoryRepository,
                                 CustomCategoryRepository customCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.customCategoryRepository = customCategoryRepository;
    }

    @Override
    public CategoryResponse creatCategory(CategoryRequest request) {

        validateAndCorrectDataCategory(request);

        if (request.getParentId().equals(PARENT_CATEGORY)) {
            return createParentCategory(request);
        }

        return createChildCategory(request);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        validateAndCorrectDataCategory(request);

        if (request.getParentId().equals(PARENT_CATEGORY)) {
            return updateParentCategory(id, request);
        }
        return updateChildCategory(id, request);
    }

    @Override
    public void deleteCategory(Long id) {

        Optional<Category> categoryById = categoryRepository.findById(id);
        if (categoryById.isEmpty()) {
            throw new PMSException(EnumPMSException.CATEGORY_NOT_EXISTED);
        }

        List<Category> categoryByParentId = customCategoryRepository.findCategoryByParentId(id);

        if (!categoryByParentId.isEmpty()) {
            throw new PMSException(EnumPMSException.CATEGORY_CANNOT_DELETED);
        }

        categoryRepository.deleteById(id);
    }

    private CategoryResponse createParentCategory(CategoryRequest request) {

        Category newCategory = new Category();
        newCategory.setName(request.getName());
        newCategory.setDescription(request.getDescription());
        newCategory.setCreateTime(Timestamp.from(Instant.now()));
        newCategory.setCreateBy(SecurityUtils.getCurrentUsername());
        newCategory.setParentId(null);

        checkDuplicateDepartment(request);

        newCategory = categoryRepository.save(newCategory);

        return ObjectMapperUtils.map(newCategory, CategoryResponse.class);
    }

    private CategoryResponse createChildCategory(CategoryRequest request) {
        Category newCategory = new Category();
        newCategory.setName(request.getName());
        newCategory.setDescription(request.getDescription());
        newCategory.setCreateTime(Timestamp.from(Instant.now()));
        newCategory.setCreateBy(SecurityUtils.getCurrentUsername());

        Optional<Category> parentCategory = categoryRepository.findById(request.getParentId());

        if (parentCategory.isEmpty()) {
            throw new PMSException(EnumPMSException.PARENT_CATEGORY_NOT_EXISTED);
        }

        newCategory.setParentId(parentCategory.get());

        checkDuplicateDepartment(request);

        newCategory = categoryRepository.save(newCategory);

        return CategoryMapper.toDTO(newCategory);
    }

    private CategoryResponse updateParentCategory(Long id, CategoryRequest request) {

        Optional<Category> categoryById = categoryRepository.findById(id);

        if (categoryById.isEmpty()) {
            throw new PMSException(EnumPMSException.CATEGORY_NOT_EXISTED);
        }

        if (!id.equals(request.getId())) {
            throw new PMSException(EnumPMSException.ID_NOT_EQUALS);
        }

        Category category = categoryById.get();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setUpdateBy(SecurityUtils.getCurrentUsername());
        category.setUpdateTime(Timestamp.from(Instant.now()));
        category.setParentId(null);

        checkDuplicateDepartment(request);

        category = categoryRepository.save(category);

        return CategoryMapper.toDTO(category);
    }

    private CategoryResponse updateChildCategory(Long id, CategoryRequest request) {

        Optional<Category> categoryById = categoryRepository.findById(id);

        if (categoryById.isEmpty()) {
            throw new PMSException(EnumPMSException.CATEGORY_NOT_EXISTED);
        }

        if (!id.equals(request.getId())) {
            throw new PMSException(EnumPMSException.ID_NOT_EQUALS);
        }

        if (request.getParentId().equals(request.getId())) {
            throw new PMSException(EnumPMSException.ID_EQUALS);
        }

        Category category = categoryById.get();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setUpdateBy(SecurityUtils.getCurrentUsername());
        category.setUpdateTime(Timestamp.from(Instant.now()));

        Optional<Category> parentCategory = categoryRepository.findById(request.getParentId());

        if (parentCategory.isEmpty()) {
            throw new PMSException(EnumPMSException.PARENT_CATEGORY_NOT_EXISTED);
        }

        category.setParentId(parentCategory.get());

        checkDuplicateDepartment(request);

        category = categoryRepository.save(category);

        return CategoryMapper.toDTO(category);
    }


    private void validateAndCorrectDataCategory(CategoryRequest request) {

        if (request == null
                || ObjectUtils.isEmpty(request.getName())
                || ObjectUtils.isEmpty(request.getDescription())
                || ObjectUtils.isEmpty(request.getParentId())) {
            throw new PMSException(EnumPMSException.CATEGORY_INVALID);
        }

        request.setName(StringUtil.removeWhitespace(request.getName()));
        request.setDescription(StringUtil.removeWhitespace(request.getDescription()));
    }

    private void checkDuplicateDepartment(CategoryRequest request) {

        Category response = customCategoryRepository.findExistCategory(request);
        if (!ObjectUtils.isEmpty(response) && (request.getId() == null || !request.getId().equals(response.getId()))) {
            throw new PMSException(EnumPMSException.CATEGORY_EXISTED);
        }
    }
}
