package com.zy.common.model.ui;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zy.common.model.query.Page;

public class Grid<T> {

	private final List<T> data;
	private final Long total;
	private final Integer pageNumber;
	private final Integer pageSize;

	public Grid(Page<T> page) {
		super();
		this.data = page.getData();
		this.total = page.getTotal();
		this.pageNumber = page.getPageNumber();
		this.pageSize = page.getPageSize();
	}

	@JsonProperty("startRow")
	public long getStartRow() {
		return this.getOffset() + 1;
	}

	@JsonProperty("recordsFiltered")
	public long getFiltered() {
		return this.getTotal();
	}

	@JsonProperty("recordsTotal")
	public long getTotal() {
		return total;
	}

	@JsonProperty("data")
	public List<T> getData() {
		return data;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Long getOffset() {
		if (pageNumber == null || pageSize == null)
			return null;
		return ((long) pageNumber) * pageSize;
	}

	public Integer getPageCount() {
		if (pageNumber == null || pageSize == null)
			return null;
		return (int) (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
	}

}