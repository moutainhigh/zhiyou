package com.zy.common.model.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page<T> implements Serializable {
	private static final long serialVersionUID = 867755909294344406L;
	private List<T> data = new ArrayList<>();
	private Long total;
	private Integer pageNumber;
	private Integer pageSize;

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getOffset() {
		if(pageNumber == null || pageSize == null)
			return null;
		return ((long) pageNumber) * pageSize;
	}
	
	public Integer getPageCount() {
		if(pageNumber == null || pageSize == null)
			return null;
		return (int) (total % pageSize == 0 ? total / pageSize : total / pageSize + 1); 
	}
}
