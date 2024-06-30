package com.binhnd.pmsbe.mapper;

import com.binhnd.pmsbe.dto.response.CategoryResponse;
import com.binhnd.pmsbe.entity.Category;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    public static CategoryResponse toDTO(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());

        Category parentCategory = category.getParentId();

        if (!ObjectUtils.isEmpty(parentCategory)) {
            response.setParentId(parentCategory.getId());
            response.setParentName(parentCategory.getName());
        }

        response.setCreateTime(category.getCreateTime());
        response.setUpdateTime(category.getUpdateTime());
        response.setCreateBy(category.getCreateBy());
        response.setUpdateBy(category.getUpdateBy());
        return response;
    }

    public static List<CategoryResponse> mapAll(List<Category> categories) {
        List<CategoryResponse> responses = new ArrayList<>();
        for (Category category : categories) {
            responses.add(toDTO(category));
        }
        return responses;
    }
}
