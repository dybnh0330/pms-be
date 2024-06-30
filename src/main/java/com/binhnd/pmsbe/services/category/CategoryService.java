package com.binhnd.pmsbe.services.category;

import com.binhnd.pmsbe.dto.request.SearchSortDto;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {

    CategoryResponse findCategoryById(Long id);

    List<CategoryResponse> findAllCategory();

    Page<CategoryResponse> findAllPageCategoryByType(Long parentId, SearchSortPageableDTO dto);

    Page<CategoryResponse> findAllPageCategory(SearchSortPageableDTO dto);

    List<CategoryResponse> findCategoryByParentId(Long parentId);

    List<CategoryResponse> findAllParentCategory(SearchSortDto dto);
}
