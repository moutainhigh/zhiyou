package com.zy.common.support.file;

public class ExcelField {

	private String name;
	private Class<?> type;
	private String title;
	private short align;
	private int width;

	public ExcelField() {
		super();
	}

	public ExcelField(String name, Class<?> type) {
		super();
		this.name = name;
		this.type = type;
	}

	public ExcelField(String name, Class<?> type, String title, short align, int width) {
		super();
		this.name = name;
		this.type = type;
		this.title = title;
		this.align = align;
		this.width = width;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public short getAlign() {
		return align;
	}

	public void setAlign(short align) {
		this.align = align;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

}
