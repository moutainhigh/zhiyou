package com.zy.admin.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zy.common.exception.ValidationException;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.support.AliyunOssSupport;
import com.zy.model.Constants;
import com.zy.model.Principal;

@Controller
@RequestMapping("/image")
public class ImageController {

	final Logger logger = LoggerFactory.getLogger(ImageController.class);

	@Autowired
	private AliyunOssSupport aliyunOssSupport;

	private void checkImageUpload(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new ValidationException("图片不能为空");
		}
		long fileSize = file.getSize();
		String originalFilename = file.getOriginalFilename();
		String ext = StringUtils.lowerCase(FilenameUtils.getExtension(originalFilename));
		String[] allowedExts = new String[] { "jpg", "png", "jpeg", "gif", "webp" };
		List<String> exts = new ArrayList<String>();
		for (String str : allowedExts) {
			exts.add(str);
		}
		if (!exts.contains(ext)) {
			throw new ValidationException("图片后缀错误,必须为" + StringUtils.join(allowedExts, ","));
		}
		if (fileSize > 4 * 1024 * 1024) {
			throw new ValidationException("图片大小不能超过4MB");
		}
	}

	@ResponseBody
	@RequestMapping("/upload")
	public Result<?> upload(MultipartFile file, HttpServletRequest request, Model model, Principal principal) {
		try {
			checkImageUpload(file);
			String imageUrl = aliyunOssSupport.putPublicObject(Constants.ALIYUN_BUCKET_NAME_IMAGE, "image/", file, Constants.ALIYUN_URL_IMAGE);// 上传阿里云OSS
			return new ResultBuilder<>().data(imageUrl).build();
		} catch (Exception e) {
			logger.error("上传图片失败", e);
			return ResultBuilder.error(StringEscapeUtils.escapeXml(e.getMessage()));
		}
	}
	
}
