package com.zy.common.support.file;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface ExportHandler {

	void handleExport(OutputStream os, String fileName, List<Map<String, Object>> dataList);

}
