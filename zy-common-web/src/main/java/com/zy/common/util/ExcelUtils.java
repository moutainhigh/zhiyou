package com.zy.common.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.convert.ConversionService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class ExcelUtils {

	private static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";

	public static void exportExcel(List<Map<String, Object>> dataList, List<String> headers, OutputStream os) throws IOException {
		Workbook wb = new SXSSFWorkbook(1000); // 创建wb

		for (String header : headers) {
			boolean isDate = dataList.stream().map(v -> v.get(header)).filter(Objects::nonNull).anyMatch(v -> v.getClass() == Date.class);
			createStyle(wb, isDate); // 创建样式
		}

		createStyle0(wb);

		Sheet sheet = wb.createSheet(); // 创建sheet

		Row headerRow = sheet.createRow(0); // 创建header row

		// 写入header row
		int columnIndex = 0;
		int styleIndex = headers.size() + 1;
		for (String header : headers) {
			Cell cell = headerRow.createCell(columnIndex++);
			cell.setCellValue(header);
			cell.setCellStyle(cell.getSheet().getWorkbook().getCellStyleAt((short) styleIndex));
		}

		int rowIndex = 1;
		for (Map<String, Object> data : dataList) {
			Row row = sheet.createRow(rowIndex++);
			columnIndex = 0;
			for (String header : headers) {
				Cell cell = row.createCell(columnIndex++);
				cell.setCellStyle(wb.getCellStyleAt((short) columnIndex));
				Object value = data.get(header);
				if (value == null) {
					cell.setCellType(Cell.CELL_TYPE_BLANK);
				} else {
					setCellValue(cell, value.getClass(), value);
				}

			}
		}

		for (int i = 0; i < headers.size(); i++) {
			sheet.autoSizeColumn(i);
		}
		try {
			wb.write(os);
		} finally {
			((SXSSFWorkbook) wb).dispose();
		}

	}

	public static <T> void exportExcel(List<T> dataList, Class<T> dataClass, OutputStream os) throws IOException {

		Workbook wb = new SXSSFWorkbook(1000); // 创建wb
		List<Field> fields = Reflections.getFields(dataClass).stream()
				.filter(Reflections::isNotStaticField)
				.sorted((u, v) -> {
					io.gd.generator.annotation.Field fu = u.getAnnotation(io.gd.generator.annotation.Field.class);
					io.gd.generator.annotation.Field fv = v.getAnnotation(io.gd.generator.annotation.Field.class);
					int nu = fu == null ? Integer.MAX_VALUE : fu.order();
					int nv = fv == null ? Integer.MAX_VALUE : fv.order();
					if (nu == nv) {
						return 0;
					} else if (nu > nv) {
						return 1;
					} else {
						return -1;
					}
				})
				.collect(Collectors.toList());

		for (Field field : fields) {
			createStyle(wb, field.getType() == Date.class); // 创建样式
		}

		createStyle0(wb);

		Sheet sheet = wb.createSheet(); // 创建sheet

		Row headerRow = sheet.createRow(0); // 创建header row

		// 写入header row
		int columnIndex = 0;
		int styleIndex = fields.size() + 1;
		for (Field field : fields) {
			io.gd.generator.annotation.Field f = field.getAnnotation(io.gd.generator.annotation.Field.class);
			String header;
			if (f != null) {
				header = f.label();
			} else {
				header = field.getName();
			}
			Cell cell = headerRow.createCell(columnIndex++);
			cell.setCellValue(header);
			cell.setCellStyle(cell.getSheet().getWorkbook().getCellStyleAt((short) styleIndex));
		}

		int rowIndex = 1;
		for (T data : dataList) {
			Row row = sheet.createRow(rowIndex++);
			columnIndex = 0;
			for (Field field : fields) {
				Cell cell = row.createCell(columnIndex++);
				cell.setCellStyle(wb.getCellStyleAt((short) columnIndex));
				Object value = Reflections.getFieldValue(data, field.getName());
				if (value == null) {
					cell.setCellType(Cell.CELL_TYPE_BLANK);
				} else {
					setCellValue(cell, field.getType(), value);
				}

			}
		}

		for (int i = 0; i < fields.size(); i++) {
			sheet.autoSizeColumn(i);
		}
		try {
			wb.write(os);
		} finally {
			((SXSSFWorkbook) wb).dispose();
		}
	}

	private static void createStyle0(Workbook wb) {
		CellStyle style0 = wb.createCellStyle(); //style5
		style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style0.setBorderRight(CellStyle.BORDER_THIN);
		style0.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style0.setBorderLeft(CellStyle.BORDER_THIN);
		style0.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style0.setBorderTop(CellStyle.BORDER_THIN);
		style0.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style0.setBorderBottom(CellStyle.BORDER_THIN);
		style0.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		// style.setWrapText(true);
		style0.setAlignment(CellStyle.ALIGN_CENTER);
		style0.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style0.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font font0 = wb.createFont();
		font0.setFontName("Arial");
		font0.setFontHeightInPoints((short) 10);
		font0.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style0.setFont(font0);
	}

	private static void createStyle(Workbook wb, boolean isDate) {

		CellStyle style = wb.createCellStyle(); //style1
		if (isDate) {
			DataFormat format = wb.createDataFormat();
			style.setDataFormat(format.getFormat(DEFAULT_DATE_FORMAT));
		}
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		Font font = wb.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);
		style.setFont(font);
	}


	private static void setCellValue(Cell cell, Class<?> type, Object value) {
		try {
			Class<?> valueClass = type;
			if (String.class.isAssignableFrom(valueClass)) {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue((String) value);
			} else if (Integer.class.isAssignableFrom(valueClass) || int.class.isAssignableFrom(valueClass)) {
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue((Integer) value);
			} else if (Long.class.isAssignableFrom(valueClass) || long.class.isAssignableFrom(valueClass)) {
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue((Long) value);
			} else if (Double.class.isAssignableFrom(valueClass) || double.class.isAssignableFrom(valueClass)) {
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue((Double) value);
			} else if (Float.class.isAssignableFrom(valueClass) || float.class.isAssignableFrom(valueClass)) {
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue((Float) value);
			} else if (Character.class.isAssignableFrom(valueClass) || char.class.isAssignableFrom(valueClass)) {
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue((Character) value);
			} else if (Byte.class.isAssignableFrom(valueClass) || byte.class.isAssignableFrom(valueClass)) {
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue((Byte) value);
			} else if (BigDecimal.class.isAssignableFrom(valueClass)) {
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(((BigDecimal) value).doubleValue());
			} else if (BigInteger.class.isAssignableFrom(valueClass)) {
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(((BigInteger) value).doubleValue());
			} else if (Date.class.isAssignableFrom(valueClass)) {
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue((Date) value);
			} else if (Boolean.class.isAssignableFrom(valueClass) || boolean.class.isAssignableFrom(valueClass)) {
				cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
				cell.setCellValue((boolean) value);
			} else if (Enum.class.isAssignableFrom(valueClass)) {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(value.toString());
			} else { // 其他类型
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(value.toString());
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("行" + (cell.getRowIndex() + 1) + "列" + (cell.getColumnIndex() + 1) + "写入错误:"
					+ e.getMessage());
		}
	}


	public static <T> List<T> importExcel(InputStream is, Class<T> dataClass, ExcelImportConfig config) throws IOException {
		Workbook wb = new XSSFWorkbook(is);
		List<Sheet> sheets = new ArrayList<>();
		if (config.isAllSheets()) {
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				sheets.add(wb.getSheetAt(i));
			}
		} else {
			sheets.add(wb.getSheetAt(0));
		}

		List<Field> fields = Reflections.getFields(dataClass).stream().filter(Reflections::isNotStaticField).collect(Collectors.toList());

		List<T> dataList = new ArrayList<>();
		for (Sheet sheet : sheets) {
			int startRowNum = config.getHeaderRow();
			int endRowNum = sheet.getLastRowNum();
			for (int rowIndex = startRowNum; rowIndex <= endRowNum; rowIndex++) {
				Row row = sheet.getRow(rowIndex);

				int columnIndex = 0;
				T t;
				try {
					t = dataClass.newInstance();
				} catch (Exception e) {
					throw new RuntimeException(dataClass.getName() + " instance error");
				}
				if (row != null) {
					for (Field field : fields) {
						String name = field.getName();
						Class<?> type = field.getType();
						Cell cell = row.getCell(columnIndex);
						Object value = getCellValue(cell, type);
						Reflections.setFieldValue(t, name, value);
						columnIndex++;
					}
				} else if (!config.isIgnoreBlankRow()) {
					throw new IllegalArgumentException("行" + (rowIndex + 1) + "为空,请确保导入的excel中间没有空行");
				}
				dataList.add(t);
			}

		}

		return dataList;

	}

	private static Object getCellValue(Cell cell, Class<?> type) {
		if (cell == null) {
			return null;
		}
		Object value = null;
		// TODO 严格模式
		/*if (cell != null) {
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
		}*/
		return value;
	}


	public static class ExcelImportConfig {
		private int headerRow = 1; // 头总行数  默认1

		private boolean allSheets = false; // false 读取所有sheet; true 只读取第一个sheet

		private boolean ignoreBlankRow = false;  // false 出现空行时抛出异常; true 出现空行时忽略

		private ConversionService conversionService; // 转换器 可选

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

		public boolean isIgnoreBlankRow() {
			return ignoreBlankRow;
		}

		public void setIgnoreBlankRow(boolean ignoreBlankRow) {
			this.ignoreBlankRow = ignoreBlankRow;
		}

		public ConversionService getConversionService() {
			return conversionService;
		}

		public void setConversionService(ConversionService conversionService) {
			this.conversionService = conversionService;
		}
	}

}
