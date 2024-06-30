package com.binhnd.pmsbe.services.excel;

import org.springframework.web.multipart.MultipartFile;

public interface ImportExcelService {

    Object importExcel(MultipartFile file);
}
