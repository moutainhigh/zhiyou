package com.zy.common.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.InputStream;

public final class JsonUtils {
	
	private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	static {
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
	}

	public static String toJson(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			logger.error("write to json string error:" + object, e);
			throw new RuntimeException(e);
		}
	}


	
	public static <T> T fromJson(String jsonString, Class<T> valueType) {
		Assert.notNull(valueType);
		if (StringUtils.isBlank(jsonString)) {
			return null;
		}
		try {
			return mapper.readValue(jsonString, valueType);
		} catch (Exception e) {
			logger.error("parse json string error:" + jsonString, e);
			throw new RuntimeException(e);
		}
	}

	public static <T> T fromJson(InputStream is, Class<T> valueType) {
		Assert.notNull(valueType);
		Assert.notNull(is);
		try {
			return mapper.readValue(is, valueType);
		} catch (Exception e) {
			logger.error("parse json string error", e);
			throw new RuntimeException(e);
		}
	}
	
	public static <T> T fromJson(String jsonString, TypeReference<T> typeReference) {
		Assert.notNull(typeReference);
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}
		try {
			return mapper.readValue(jsonString, typeReference);
		} catch (Exception e) {
			logger.error("parse json string error:" + jsonString, e);
			throw new RuntimeException(e);
		}
	}

}
