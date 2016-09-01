package com.zy.common.extend;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

public class StringToStringConverter implements GenericConverter {

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, String.class));
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (source == null || ((String) source).length() == 0) {
			return null;
		}
		StringBinder stringBinder = targetType.getAnnotation(StringBinder.class);
		if (stringBinder == null) {
			return source;
		} else {
			String str = (String) source;
			if (stringBinder.trim()) {
				str = StringUtils.trimToNull(str);
			}

			if (stringBinder.xss()) {
				str = StringEscapeUtils.escapeXml(str);
			}
			return str;
		}
	}

}
