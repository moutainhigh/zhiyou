package com.zy.common.support.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * 导入Excel文件（支持“XLS”和“XLSX”格式）
 * 
 * @author 薛定谔的猫
 * @version 2013-10-16
 */
public class TestExcelImportExportHandler extends ExcelImportHandler {

	public TestExcelImportExportHandler() {
		super();
		fields.add(new ExcelField("a", Integer.class));
		fields.add(new ExcelField("b", String.class));
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		if(false) {
			ImportHandler testExcelImportHandler = new TestExcelImportExportHandler();
			File file = new File("D:/1.xlsx");
			InputStream is = null;
			try {
				 is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			List<Map<String, Object>>  result = testExcelImportHandler.handleImport(is, file.getName());
			
			ExportHandler testExcelExportHandler = new TestExcelExportHandler();
			File file2 = new File("D:/2.xlsx");
			OutputStream os = null;
			
			try {
				os=new FileOutputStream(file2);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			testExcelExportHandler.handleExport(os, file2.getName(), result);
			try {
				os.flush();
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.setProperty("spring.profiles.active", "production");  
			ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext-file.xml");
			ImportHandler testExcelImportHandler = (ImportHandler) ac.getBean("test2ExcelImportHandler");
			File file = new File("D:/1.xlsx");
			InputStream is = null;
			try {
				 is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			List<Map<String, Object>>  result = testExcelImportHandler.handleImport(is, file.getName());
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			ExcelExportHandler testExcelExportHandler = (ExcelExportHandler) ac.getBean("test2ExcelExportHandler");
			File file2 = new File("D:/2.xlsx");
			OutputStream os = null;
			
			try {
				os=new FileOutputStream(file2);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			testExcelExportHandler.handleExport(os, file2.getName(), result);
			try {
				os.flush();
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ac.close();
		}
	}

}

class TestExcelExportHandler extends ExcelExportHandler {
	public TestExcelExportHandler() {
		super();
		fields.add(new ExcelField("a", Integer.class));
		fields.add(new ExcelField("b", String.class));
	}
}
