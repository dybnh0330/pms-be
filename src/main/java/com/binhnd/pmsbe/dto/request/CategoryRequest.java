package com.binhnd.pmsbe.dto.request;

import lombok.Data;

@Data
public class CategoryRequest {
    private Long id;
    private String name;
    private String description;
    private Long parentId;
}
