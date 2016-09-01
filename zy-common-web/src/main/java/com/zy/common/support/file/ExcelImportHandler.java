package com.zy.common.support.file;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * 导入Excel文件（支持“XLS”和“XLSX”格式）
 * 
 * @author 薛定谔的猫
 * @version 2013-10-16
 */
public class ExcelImportHandler implements ImportHandler {

	protected int headerRow = 1; // 头总行数  默认1

	protected boolean allSheets = false; // false 读取所有sheet; true 只读取第一个sheet 

	protected boolean ignoreBlankRow = true;  // false 出现空行时抛出异常; true 出现空行时忽略 

	@Autowired
	protected ConversionService conversionService;
	
	protected List<ExcelField> fields = new ArrayList<ExcelField>(); // 导入field

	@Override
	public List<Map<String, Object>> handleImport(InputStream is, String fileName) {
		Assert.notEmpty(fields);
		Workbook wb = createWorkbook(fileName, is); // 创建excel工作薄
		List<Sheet> sheets = resolveSheets(wb); // 获取sheets
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for (Sheet sheet : sheets) {
			dataList.addAll(parseSheet(sheet)); // 提取每个sheet数据
		}
		return dataList;
	}

	/**
	 * 从流创建excel工作薄
	 * 
	 * @param fileName
	 * @param is
	 * @return
	 */
	private Workbook createWorkbook(String fileName, InputStream is) {
		Workbook wb = null;
		if (StringUtils.isBlank(fileName)) {
			throw new IllegalArgumentException("导入文档为空!");
		} else if (fileName.toLowerCase().endsWith("xls")) {
			try {
				wb = new HSSFWorkbook(is);
			} catch (IOException e) {
				throw new IllegalArgumentException("读取xls文件错误,请检查文件后缀是否正确!");
			}
		} else if (fileName.toLowerCase().endsWith("xlsx")) {
			try {
				wb = new XSSFWorkbook(is);
			} catch (IOException e) {
				throw new IllegalArgumentException("读取xlsx文件错误,请检查文件后缀是否正确!");
			}
		} else {
			throw new IllegalArgumentException("文档格式不正确!");
		}
		if (wb.getNumberOfSheets() == 0) {
			throw new IllegalArgumentException("文档中没有工作表!");
		}
		return wb;
	}

	private List<Sheet> resolveSheets(Workbook wb) {
		List<Sheet> sheetList = new ArrayList<Sheet>();
		if (allSheets) {
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				sheetList.add(wb.getSheetAt(i));
			}
		} else {
			sheetList.add(wb.getSheetAt(0));
		}
		return sheetList;
	}

	private Object parseCell(Cell cell, ExcelField field) {
		Class<?> type = field.getType();
		Object value = null;
		if (cell != null) {
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				value = cell.getNumericCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				value = cell.getStringCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				value = cell.getCellFormula();
			} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
				value = cell.getBooleanCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
				value = cell.getErrorCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				value = null;
			}
		}
		if (value != null) {
			try {
				if (type == String.class) {
					value = String.valueOf(value.toString());
				} else if (type == Integer.class || type == int.class) {
					value = Double.valueOf(value.toString()).intValue();
				} else if (type == Long.class || type == long.class) {
					value = Double.valueOf(value.toString()).longValue();
				} else if (type == Double.class || type == double.class) {
					value = Double.valueOf(value.toString());
				} else if (type == Float.class || type == float.class) {
					value = Float.valueOf(value.toString());
				} else if (type == Date.class) {
					value = DateUtil.getJavaDate((Double) value);
				} else if(type == BigDecimal.class) {
					value = new BigDecimal(value.toString());
				} else if(type == Boolean.class || type == boolean.class) {
					value =conversionService.convert(value, Boolean.class);
				} else {
					throw new IllegalArgumentException("不支持的导入字段类型" + type.getName());
				}
			} catch (Exception e) {
				throw new IllegalArgumentException("行" + (cell.getRowIndex() + 1) + "列" + (cell.getColumnIndex() + 1) + "读取错误:"
						+ e.getMessage());
			}
		}
		return value;
	}
	
	private Map<String, Object> parseRow(Row row) {
		int columnIndex = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		if (row != null) {
			for (ExcelField field : fields) {
				String name = field.getName();
				Cell cell = row.getCell(columnIndex);
				Object value = parseCell(cell, field);
				if(value != null) {
					map.put(name, value);
				}
				columnIndex++;
			}
		}
		return map;
	}

	private List<Map<String, Object>> parseSheet(Sheet sheet) {
		List<Map<String, Object>> dataList = new ArrayList<>();
		int startRowNum = headerRow;
		int endRowNum = sheet.getLastRowNum();
		for (int rowIndex = startRowNum; rowIndex <= endRowNum; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			Map<String, Object> map = parseRow(row);
			if(!(map == null || map.isEmpty())) {
				dataList.add(map);
			} else if(!ignoreBlankRow) {
				throw new IllegalArgumentException("行" + (rowIndex + 1) + "为空,请确保导入的excel中间没有空行");
			}
		}
		return dataList;
	}

	public int getHeaderRow() {
		return headerRow;
	}

	public void setHeaderRow(int headerRow) {
		this.headerRow = headerRow;
	}

	public boolean isAllSheets() {
		return allSheets;
	}

	public void setAllSheets(boolean allSheets) {
		this.allSheets = allSheets;
	}

	public List<ExcelField> getFields() {
		return fields;
	}

	public void setFields(List<ExcelField> fields) {
		this.fields = fields;
	}

	public boolean isIgnoreBlankRow() {
		return ignoreBlankRow;
	}

	public void setIgnoreBlankRow(boolean ignoreBlankRow) {
		this.ignoreBlankRow = ignoreBlankRow;
	}

}
