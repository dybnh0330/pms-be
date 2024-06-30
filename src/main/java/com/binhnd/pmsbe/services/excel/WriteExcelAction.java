package com.binhnd.pmsbe.services.excel;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

public interface WriteExcelAction {

    void createWriter();

    <T> void appendData(List<T> datas);

    Workbook write();

    String getSheetName();

    String getTitle();

    short getHeaderHeight();

    short getTitleHeight();

    void drawFooter();

}
