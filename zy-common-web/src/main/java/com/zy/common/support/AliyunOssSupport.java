package com.zy.common.support;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.zy.common.util.Identities;
import com.zy.common.util.ValidateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static com.zy.common.util.ValidateUtils.validate;

public class AliyunOssSupport implements DisposableBean {
	
	private final String DEFAULT_ENDPOINT = "http://oss-cn-hangzhou.aliyuncs.com";
	
	private final OSSClient ossClient;
	
	private String group;
	
	public AliyunOssSupport(String endpoint, String accessKeyId, String accessKeySecret, String group) {
		if(endpoint == null)
			endpoint = DEFAULT_ENDPOINT;
		ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
	}
	
	public String putPublicObject(String bucketName, String path, MultipartFile multipartFile, String domain) {
		ValidateUtils.validate(multipartFile, v -> v != null && !v.isEmpty(), "multipart file is empty");
		
		ValidateUtils.validate(domain, ValidateUtils.NOT_BLANK.and(v -> v.startsWith("http")), "domain " + domain + " is invalid");
		ValidateUtils.validate(path, ValidateUtils.NOT_BLANK.and(v -> !v.startsWith("/")).and(v -> v.matches("[/\\w]+")), "path " + path + " is invalid");
	    InputStream inputStream;
		try {
			inputStream = multipartFile.getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	    ObjectMetadata objectMetadata = new ObjectMetadata();
	    objectMetadata.setContentLength(multipartFile.getSize());
	    objectMetadata.setContentType(multipartFile.getContentType());
	    if(!path.endsWith("/")) {
	    	path += "/";
	    }
	    String key = path + Identities.uuid();
	    if(StringUtils.isNotBlank(group)){
	    	key = path + group + "/" + Identities.uuid();
	    }
	    @SuppressWarnings("unused")
		PutObjectResult putObjectResult = ossClient.putObject(bucketName, key, inputStream, objectMetadata);
	    if(!domain.endsWith("/")) {
	    	domain += "/";
	    }
	    return domain + key;
	}
	
	public String putPublicObject(String bucketName, String path, InputStream inputStream, long contentLength, String contentType, String domain) {
		ValidateUtils.validate(domain, ValidateUtils.NOT_BLANK.and(v -> v.startsWith("http")), "domain " + domain + " is invalid");
		ValidateUtils.validate(path, ValidateUtils.NOT_BLANK.and(v -> !v.startsWith("/")).and(v -> v.matches("[/\\w]+")), "path " + path + " is invalid");
	    ObjectMetadata objectMetadata = new ObjectMetadata();
	    objectMetadata.setContentLength(contentLength);
	    objectMetadata.setContentType(contentType);
	    if(!path.endsWith("/")) {
	    	path += "/";
	    }
		String key = path + Identities.uuid();
		if(StringUtils.isNotBlank(group)){
	    	key = path + group + "/" + Identities.uuid();
	    }
	    @SuppressWarnings("unused")
		PutObjectResult putObjectResult = ossClient.putObject(bucketName, key, inputStream, objectMetadata);
	    if(!domain.endsWith("/")) {
	    	domain += "/";
	    }
	    return domain + key;
	}

	@Override
	public void destroy() throws Exception {
		ossClient.shutdown();
	}
}
