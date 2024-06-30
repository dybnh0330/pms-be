package com.binhnd.pmsbe.dto.request;

import lombok.Data;

@Data
public class SearchSortDto {
    private String searchText;
    private String sortColumn;
    private String sortDirection;
}
