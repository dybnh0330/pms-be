package com.binhnd.pmsbe.services.category;

import com.binhnd.pmsbe.dto.request.CategoryRequest;
import com.binhnd.pmsbe.dto.response.CategoryResponse;

public interface CategoryCUService {

    CategoryResponse creatCategory(CategoryRequest request);
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);

}
