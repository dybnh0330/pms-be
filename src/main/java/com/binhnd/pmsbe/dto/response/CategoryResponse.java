package com.binhnd.pmsbe.dto.response;

import com.binhnd.pmsbe.entity.Category;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private Long parentId;
    private String parentName;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String createBy;
    private String updateBy;
}
