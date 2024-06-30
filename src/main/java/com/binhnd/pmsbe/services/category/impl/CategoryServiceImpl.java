package com.binhnd.pmsbe.services.category.impl;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.dto.request.SearchSortDto;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.CategoryResponse;
import com.binhnd.pmsbe.entity.Category;
import com.binhnd.pmsbe.mapper.CategoryMapper;
import com.binhnd.pmsbe.repositories.CategoryRepository;
import com.binhnd.pmsbe.repositories.custom_repositories.CustomCategoryRepository;
import com.binhnd.pmsbe.services.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CustomCategoryRepository customCategoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CustomCategoryRepository customCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.customCategoryRepository = customCategoryRepository;
    }

    @Override
    public CategoryResponse findCategoryById(Long id) {

        Optional<Category> categoryById = categoryRepository.findById(id);
        if (categoryById.isEmpty()) {
            throw new PMSException(EnumPMSException.CATEGORY_NOT_EXISTED);
        }

        return CategoryMapper.toDTO(categoryById.get());
    }

    @Override
    public List<CategoryResponse> findAllCategory() {

        List<CategoryResponse> responses = new ArrayList<>();
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            responses.add(CategoryMapper.toDTO(category));
        }

        return responses;
    }

    @Override
    public Page<CategoryResponse> findAllPageCategoryByType(Long parentId, SearchSortPageableDTO dto) {

        Optional<Category> parentCategory = categoryRepository.findById(parentId);
        if (parentCategory.isEmpty()) {
            throw new PMSException(EnumPMSException.CATEGORY_NOT_EXISTED);
        }

        return customCategoryRepository.findAllPageCategoryByParentId(parentId, dto);
    }

    @Override
    public Page<CategoryResponse> findAllPageCategory(SearchSortPageableDTO dto) {
        return customCategoryRepository.findAllCategoryPage(dto);
    }

    @Override
    public List<CategoryResponse> findCategoryByParentId(Long parentId) {

        List<Category> categoryByParentId = customCategoryRepository.findCategoryByParentId(parentId);

        Optional<Category> parentCategory = categoryRepository.findById(parentId);

        if (parentCategory.isEmpty()) {
            throw new PMSException(EnumPMSException.CATEGORY_NOT_EXISTED);
        }

        if (categoryByParentId.isEmpty()) {
            return Collections.emptyList();
        }

        List<CategoryResponse> responses = new ArrayList<>();

        for (Category category : categoryByParentId) {
            responses.add(CategoryMapper.toDTO(category));
        }

        return responses;
    }

    @Override
    public List<CategoryResponse> findAllParentCategory(SearchSortDto dto) {

        List<Category> allParentCategory = customCategoryRepository.findAllParentCategory(dto);

        List<CategoryResponse> responses = new ArrayList<>();
        for (Category category : allParentCategory) {
            responses.add(CategoryMapper.toDTO(category));
        }

        return responses;
    }
}
