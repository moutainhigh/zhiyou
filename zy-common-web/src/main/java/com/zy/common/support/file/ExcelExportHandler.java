package com.zy.common.support.file;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.util.Assert;

/**
 * 导出Excel文件（支持“XLS”和“XLSX”格式）
 * 
 * @author 薛定谔的猫
 * @version 2013-10-16
 */
public class ExcelExportHandler implements ExportHandler {

	protected int maxRowPerSheet = 30000;

	protected List<ExcelField> fields = new ArrayList<ExcelField>(); // 导出field

	//protected String dateFormart = "yyyy-MM-dd";

	protected String title = null;

	@Override
	public void handleExport(OutputStream os, String fileName, List<Map<String, Object>> dataList) {
		Assert.notEmpty(dataList);
		Assert.notEmpty(fields);
		Workbook wb = createWorkbook(fileName); // 创建excel工作薄
		createStyles(wb); // 创建样式
		int size = dataList.size();
		int page = (int) Math.ceil(size / (double) maxRowPerSheet);
		for (int sheetIndex = 0; sheetIndex < page; sheetIndex++) {
			Sheet sheet = wb.createSheet("Sheet" + sheetIndex);
			List<Map<String, Object>> sheetDataList = new ArrayList<>();
			int maxRowNum = (sheetIndex == page - 1) ? size : (sheetIndex + 1) * maxRowPerSheet;
			for (int i = sheetIndex * maxRowPerSheet; i < maxRowNum; i++) {
				sheetDataList.add(dataList.get(i));
			}
			addSheet(sheet, sheetDataList);
			for (int i = 0; i < fields.size(); i++) {
				sheet.autoSizeColumn(i);
			}
		}
		try {
			wb.write(os);
		} catch (IOException e) {
			throw new IllegalArgumentException("写入文件" + fileName + "错误!");
		} finally {
			if(wb instanceof SXSSFWorkbook) {
				((SXSSFWorkbook)wb).dispose();
			}
		}
	}

	/**
	 * 创建表格样式
	 * 
	 * @param wb
	 *            工作薄对象
	 * @return 样式列表
	 */
	private void createStyles(Workbook wb) {
		//DataFormat format = wb.createDataFormat();

		CellStyle style1 = wb.createCellStyle(); //style1
		style1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		//style1.setDataFormat(format.getFormat(dateFormart));
		style1.setAlignment(CellStyle.ALIGN_LEFT);
		style1.setBorderRight(CellStyle.BORDER_THIN);
		style1.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style1.setBorderLeft(CellStyle.BORDER_THIN);
		style1.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style1.setBorderTop(CellStyle.BORDER_THIN);
		style1.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style1.setBorderBottom(CellStyle.BORDER_THIN);
		style1.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		Font dataFont = wb.createFont();
		dataFont.setFontName("Arial");
		dataFont.setFontHeightInPoints((short) 10);
		style1.setFont(dataFont);

		CellStyle style2 = wb.createCellStyle(); //style2
		style2.setAlignment(CellStyle.ALIGN_CENTER);
		style2.cloneStyleFrom(style1);

		CellStyle style3 = wb.createCellStyle(); //style3
		style3.setAlignment(CellStyle.ALIGN_RIGHT);
		style3.cloneStyleFrom(style1);

		CellStyle style4 = wb.createCellStyle(); //style5
		style4.cloneStyleFrom(style1);
		// style.setWrapText(true);
		style4.setAlignment(CellStyle.ALIGN_CENTER);
		style4.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style4.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font headerFont = wb.createFont();
		headerFont.setFontName("Arial");
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style4.setFont(headerFont);
		
//		CellStyle style5 = wb.createCellStyle(); //style0
//		style5.setDataFormat(format.getFormat(dateFormart));
//		style5.setAlignment(CellStyle.ALIGN_CENTER);
//		style5.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		Font titleFont = wb.createFont();
//		titleFont.setFontName("Arial");
//		titleFont.setFontHeightInPoints((short) 16);
//		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
//		titleFont.setColor((short) 3);
//		style5.setFont(titleFont);
	}

	private Workbook createWorkbook(String fileName) {
		Workbook wb = null;
		if (StringUtils.isBlank(fileName)) {
			throw new IllegalArgumentException("导入文档为空!");
		} else if (fileName.toLowerCase().endsWith("xls")) {
			wb = new HSSFWorkbook();
		} else if (fileName.toLowerCase().endsWith("xlsx")) {
			wb = new SXSSFWorkbook(1000);
		} else {
			throw new IllegalArgumentException("文档格式不正确!");
		}
		return wb;
	}

	private void addSheet(Sheet sheet, List<Map<String, Object>> dataList) {
		addHeader(sheet);
		int rowIndex = 1;
		for (Map<String, Object> map : dataList) {
			Row row = sheet.createRow(rowIndex++);
			addRow(row, map);
		}
	}

	private void addHeader(Sheet sheet) {
		Row header = sheet.createRow(0);
		int columnIndex = 0;
		for (ExcelField field : fields) {
			Cell cell = header.createCell(columnIndex++);
			cell.setCellValue(field.getTitle());
			cell.setCellStyle(cell.getSheet().getWorkbook().getCellStyleAt((short) 4));
		}
	}

	private void addRow(Row row, Map<String, Object> map) {
		int columnIndex = 0;
		for (ExcelField field : fields) {
			Object value = map.get(field.getName());
			Cell cell = row.createCell(columnIndex++);
			addCell(cell, field, value);
		}
	}
	
	private void addCell(Cell cell, ExcelField field, Object value) {
		short align = field.getAlign();
		Class<?> type = field.getType();
		short styleIndex = align >= 1 && align <= 3 ? align : 1;
		cell.setCellStyle(cell.getSheet().getWorkbook().getCellStyleAt(styleIndex));
		try {
			if (value != null) {
				if (type != null) {
					Assert.isTrue(type.isAssignableFrom(value.getClass()), type + "和" + value.getClass() + "类型不一致");
				}
				 if (value instanceof String) {
					cell.setCellValue((String) value);
				} else if (value instanceof Integer) {
					cell.setCellValue((Integer) value);
				} else if (value instanceof Long) {
					cell.setCellValue((Long) value);
				} else if (value instanceof Double) {
					cell.setCellValue((Double) value);
				} else if (value instanceof Float) {
					cell.setCellValue((Float) value);
				} else if (value instanceof Date) {
					cell.setCellValue((Date) value);
				} else if(value instanceof BigDecimal) {
					cell.setCellValue(((BigDecimal) value).doubleValue());
				} else {
					throw new IllegalArgumentException("不被识别的类型" + value.getClass());
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("行" + (cell.getRowIndex() + 1) + "列" + (cell.getColumnIndex() + 1) + "写入错误:"
					+ e.getMessage());
		}
	}

	public int getMaxRowPerSheet() {
		return maxRowPerSheet;
	}

	public void setMaxRowPerSheet(int maxRowPerSheet) {
		this.maxRowPerSheet = maxRowPerSheet;
	}

	public List<ExcelField> getFields() {
		return fields;
	}

	public void setFields(List<ExcelField> fields) {
		this.fields = fields;
	}

//	public String getDateFormart() {
//		return dateFormart;
//	}
//
//	public void setDateFormart(String dateFormart) {
//		this.dateFormart = dateFormart;
//	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public static void main(String[] args) {
		java.sql.Timestamp t = new java.sql.Timestamp(1232312312L);
		System.out.println(t instanceof Date);
	}

}
