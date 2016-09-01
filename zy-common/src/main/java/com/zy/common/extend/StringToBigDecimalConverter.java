package com.zy.common.extend;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

public class StringToBigDecimalConverter implements ConditionalGenericConverter {

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, BigDecimal.class));
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		BigDecimalBinder bigDecimalBinder = targetType.getAnnotation(BigDecimalBinder.class);
		int fraction = bigDecimalBinder.fraction();
		RoundingMode roundingMode = bigDecimalBinder.roundingMode();
		if(StringUtils.isBlank((String) source)) {
			return null;
		} else {
			return new BigDecimal((String) source).setScale(fraction, roundingMode);
		}
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		BigDecimalBinder bigDecimalBinder = targetType.getAnnotation(BigDecimalBinder.class);
		if (bigDecimalBinder == null) {
			return false;
		}
		return true;
	}

}
