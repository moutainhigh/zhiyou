package com.zy.admin.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.ServiceException;
import com.zy.admin.model.AdminConstants;
import com.zy.common.exception.ValidationException;


public class AdminUtils {
	public static void checkImageUpload(MultipartFile file) throws ServiceException {
		if(file == null || file.isEmpty()) {
			throw new ServiceException("图片不能为空");
		}
		long fileSize = file.getSize();
		String originalFilename = file.getOriginalFilename();
		String ext = StringUtils.lowerCase(FilenameUtils.getExtension(originalFilename));
		String[] allowedExts = AdminConstants.IMAGE_UPLOAD_ALLOWED_EXTENTIONS;
		List<String> exts = new ArrayList<String>();
		for(String str : allowedExts){
			exts.add(str);
		}
		if (!exts.contains(ext)) {
			throw new ValidationException("图片后缀错误,必须为" + StringUtils.join(allowedExts, ","));
		}
		if(fileSize > AdminConstants.IMAGE_UPLOAD_MAX_SIZE) {
			throw new ServiceException("图片大小不能超过5M"); 
		}
	}
}
