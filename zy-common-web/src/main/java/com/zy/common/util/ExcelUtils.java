package com.zy.common.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ExcelUtils {
	
	private static final String DEFAULT_DATE_FORMAT =  "yyyy/MM/dd HH:mm:ss";
	
	public static <T> void exportExcel(List<T> dataList, Class<T> dataClass,  boolean isOldVersion, OutputStream os) throws IOException {
		
		Workbook wb = new SXSSFWorkbook(1000); // 创建wb
		List<Field> fields = Reflections.getFields(dataClass).stream().filter(Reflections::isNotStaticField).collect(Collectors.toList());
		createStyles(wb, fields); // 创建样式
		Sheet sheet = wb.createSheet(); // 创建sheet

		Row headerRow = sheet.createRow(0); // 创建header row
		
		 // 写入header row
		fields.stream().forEach(v -> {
			int styleIndex = fields.size() + 1;
			int columnIndex = 0;
			io.gd.generator.annotation.Field f = v.getAnnotation(io.gd.generator.annotation.Field.class);
			String header;
			if (f != null) {
				header = f.label();
			} else {
				header = v.getName();
			}
			Cell cell = headerRow.createCell(columnIndex++);
			cell.setCellValue(header);
			cell.setCellStyle(cell.getSheet().getWorkbook().getCellStyleAt((short) styleIndex));
		});
		
		int rowIndex = 1;
		for (T data : dataList) {
			Row row = sheet.createRow(rowIndex++);
			int columnIndex = 0;
			for (Field field : fields) {
				Cell cell = row.createCell(columnIndex++);
				cell.setCellStyle(wb.getCellStyleAt((short) columnIndex));
				addCell(cell, field, Reflections.getFieldValue(data, field.getName()));
			}
		}
		
		for (int i = 0; i < fields.size(); i++) {
			sheet.autoSizeColumn(i);
		}
		try {
			wb.write(os);
		} finally {
			((SXSSFWorkbook)wb).dispose();
		}
	}

	private static void createStyles(Workbook wb, List<Field> fields) {
		for(Field field : fields) {
			CellStyle style = wb.createCellStyle(); //style1
			if(field.getType() == Date.class) {
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

	
	private static void addCell(Cell cell, Field field, Object value) {
		try {
			if (value != null) {
				Class<?> valueClass = value.getClass();
				if (String.class.isAssignableFrom(valueClass)) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue((String) value);
				} else if (Integer.class.isAssignableFrom(valueClass) || int.class.isAssignableFrom(valueClass)) {
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue((double) value);
				} else if (Long.class.isAssignableFrom(valueClass) || long.class.isAssignableFrom(valueClass)) {
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue((double) value);
				} else if (Double.class.isAssignableFrom(valueClass) || double.class.isAssignableFrom(valueClass)) {
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue((double) value);
				} else if (Float.class.isAssignableFrom(valueClass) || float.class.isAssignableFrom(valueClass) ) {
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue((double) value);
				} else if (Character.class.isAssignableFrom(valueClass) || char.class.isAssignableFrom(valueClass) ) {
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue((double) value);
				} else if (Byte.class.isAssignableFrom(valueClass) || byte.class.isAssignableFrom(valueClass)) {
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue((double) value);
				}  else if(BigDecimal.class.isAssignableFrom(valueClass) || BigInteger.class.isAssignableFrom(valueClass)) {
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(((Number) value).doubleValue());
				} else if (BigDecimal.class.isAssignableFrom(valueClass)) {
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue((Date) value);
				} else if (Boolean.class.isAssignableFrom(valueClass) || boolean.class.isAssignableFrom(valueClass)) {
					cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
					cell.setCellValue((boolean) value);
				} else {
					throw new IllegalArgumentException("不被识别的类型" + valueClass);
				}
			} else {
				cell.setCellType(Cell.CELL_TYPE_BLANK);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("行" + (cell.getRowIndex() + 1) + "列" + (cell.getColumnIndex() + 1) + "写入错误:"
					+ e.getMessage());
		}
	}


}
