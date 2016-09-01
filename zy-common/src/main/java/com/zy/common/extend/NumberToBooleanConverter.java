package com.zy.common.extend;

import org.springframework.core.convert.converter.Converter;

public class NumberToBooleanConverter implements Converter<Number, Boolean> {

	@Override
	public Boolean convert(Number source) {
		if(source.intValue() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
}
