package com.zy.common.model.query;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageBuilder {
	
	private static <S, T> Page<T> copy(Page<S> page) {
		Page<T> result = new Page<>();
		result.setPageNumber(page.getPageNumber());
		result.setPageSize(page.getPageSize());
		result.setTotal(page.getTotal());
		return result;
	}
	
	public static <S, T> Page<T> copyAndConvert(Page<S> page, List<T> list) {
		Page<T> result = copy(page);
		result.setData(list);
		return result;
	}
	
	public static <S, T> Page<T> copyAndConvert(Page<S> page, Function<S, T> function) {
		Page<T> result = copy(page);
		result.setData(page.getData().stream().map(v -> function.apply(v)).collect(Collectors.toList()));
		return result;
	}

	public static <T> Page<T> empty(Integer getPageSize, Integer getPageNumber) {
		Page<T> result = new Page<>();
		result.setPageNumber(getPageNumber);
		result.setPageSize(getPageSize);
		result.setTotal(0L);
		result.setData(new ArrayList<>());
		return result;
	}
	
}
