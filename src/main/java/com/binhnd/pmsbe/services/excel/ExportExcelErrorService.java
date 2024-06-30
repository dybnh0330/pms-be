package com.binhnd.pmsbe.services.excel;

import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

public interface ExportExcelErrorService {

    Workbook downloadErrorExcel(List<T> object);
}
