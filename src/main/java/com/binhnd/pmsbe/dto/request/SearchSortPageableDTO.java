package com.binhnd.pmsbe.dto.request;

import lombok.Data;

@Data
public class SearchSortPageableDTO {

    private Integer currentPageNumber;
    private Integer pageSize;
    private String searchText;
    private String sortColumn;
    private String sortDirection;

}
