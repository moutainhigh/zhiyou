package com.zy.common.support.file;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface ImportHandler {

	List<Map<String, Object>> handleImport(InputStream is, String fileName);

}
