package com.binhnd.pmsbe.common.utils.excel_parser;

import java.util.Map;

public interface OnGetRowListener {

    void onGetRow(long rowIndex, Map<String, String> rowValue) throws InstantiationException, IllegalAccessException;
}
