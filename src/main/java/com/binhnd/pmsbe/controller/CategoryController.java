package com.binhnd.pmsbe.controller;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.common.constants.RequestAction;
import com.binhnd.pmsbe.dto.request.CategoryRequest;
import com.binhnd.pmsbe.dto.request.SearchSortDto;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;
import com.binhnd.pmsbe.dto.response.CategoryResponse;
import com.binhnd.pmsbe.services.category.CategoryCUService;
import com.binhnd.pmsbe.services.category.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.binhnd.pmsbe.common.constants.PMSConstants.PREFIX_URL;
import static com.binhnd.pmsbe.common.constants.RequestAction.CATEGORY;

@RestController
@RequestMapping(value = PREFIX_URL + CATEGORY)
public class CategoryController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CategoryCUService categoryCUService;

    private final CategoryService categoryService;

    public CategoryController(CategoryCUService categoryCUService,
                              CategoryService categoryService) {
        this.categoryCUService = categoryCUService;
        this.categoryService = categoryService;
    }

    @PostMapping(value = RequestAction.Category.CREATE_CATEGORY, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        logger.info("[POST]{}/ create a new category", PMSConstants.PREFIX_URL + "/category/create");
        CategoryResponse response = categoryCUService.creatCategory(request);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(value = RequestAction.Category.UPDATE_CATEGORY, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryResponse> updateCategory(@RequestParam Long id, @RequestBody CategoryRequest request) {
        logger.info("[POST]{}/ update a category", PMSConstants.PREFIX_URL + "/category/update?id=");
        CategoryResponse response = categoryCUService.updateCategory(id, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(value = RequestAction.Category.DELETE_CATEGORY, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteCategory(@RequestParam Long id) {
        logger.info("[POST]{}/ create a new category", PMSConstants.PREFIX_URL + "/category?id=" + id);
        categoryCUService.deleteCategory(id);
        return ResponseEntity.ok().body("\"Category deleted successfully!\"");
    }

    @GetMapping(value = RequestAction.Category.FIND_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryResponse>> findAllCategory() {
        logger.info("[GET]{}/ find all category", PMSConstants.PREFIX_URL + "/category/find-all");
        List<CategoryResponse> responses = categoryService.findAllCategory();
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping(value = RequestAction.Category.FIND_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryResponse> findCategoryById(@RequestParam Long id) {
        logger.info("[GET]{}/ find a category", PMSConstants.PREFIX_URL + "/category/find-by-id");
        CategoryResponse response = categoryService.findCategoryById(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = RequestAction.Category.FIND_BY_TYPE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryResponse>> findCategoryByParentId (@RequestParam Long type) {
        logger.info("[GET]{}/ find a category by parentId", PMSConstants.PREFIX_URL + "/category/find-by-type");
        List<CategoryResponse> responses = categoryService.findCategoryByParentId(type);
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping(value = RequestAction.Category.FIND_ALL_TYPE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryResponse>> findAllParentCategory(SearchSortDto dto) {
        logger.info("[GET]{}/ find all parent category", PMSConstants.PREFIX_URL + "/category/type/find-all");
        List<CategoryResponse> responses = categoryService.findAllParentCategory(dto);
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping(value = RequestAction.Category.FIND_PAGE_BY_TYPE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CategoryResponse>> findAllPageCategory(@RequestParam Long id, SearchSortPageableDTO dto) {
        logger.info("[GET]{}/ find all page by parent_category id", PMSConstants.PREFIX_URL + "/category/page/find-by-type?id=" + id);
        Page<CategoryResponse> responses = categoryService.findAllPageCategoryByType(id, dto);
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping(value = RequestAction.Category.FIND_ALL_PAGE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CategoryResponse>> findAllPageCategory(SearchSortPageableDTO dto) {
        logger.info("[GET]{}/ find all page category", PMSConstants.PREFIX_URL + "/category/page/find-all");
        Page<CategoryResponse> responses = categoryService.findAllPageCategory(dto);
        return ResponseEntity.ok().body(responses);
    }
}
