package com.zy.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zy.common.exception.ValidationException;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.support.AliyunOssSupport;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.util.GcUtils;

@Controller
@RequestMapping("/image")
public class ImageController {

	final Logger logger = LoggerFactory.getLogger(ImageController.class);

	@Autowired
	private AliyunOssSupport aliyunOssSupport;

	@ResponseBody
	@RequestMapping("/upload")
	public Result<?> upload(MultipartFile file, HttpServletRequest request, Model model, Principal principal) {
		try {
			GcUtils.checkImage(file);
			String imageUrl = aliyunOssSupport.putPublicObject(Constants.ALIYUN_BUCKET_NAME_IMAGE, "image/", file, Constants.ALIYUN_URL_IMAGE);// 上传阿里云OSS
			return new ResultBuilder<>().data(imageUrl).build();
		} catch (Exception e) {
			logger.error("上传图片失败", e);
			return ResultBuilder.error(StringEscapeUtils.escapeXml(e.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping("/upload2")
	public Result<?> upload2(@RequestParam(defaultValue = "200") int width, @RequestParam(defaultValue = "200") int height, MultipartFile file, HttpServletRequest request, Model model, Principal principal) {
		try {
			if (width < 0 || width > 1000 || height < 0 || height > 1000) {
				throw new ValidationException("长宽参数错误");
			}
			GcUtils.checkImage(file);
			String url = aliyunOssSupport.putPublicObject(Constants.ALIYUN_BUCKET_NAME_IMAGE, "image/", file, Constants.ALIYUN_URL_IMAGE);
			Map<String, String> result = new HashMap<>();
			result.put("image", url);
			result.put("imageThumbnail", GcUtils.getThumbnail(url, width, height));
			return ResultBuilder.result(result);
		} catch (Exception e) {
			logger.error("上传图片失败", e);
			return ResultBuilder.error(StringEscapeUtils.escapeXml(e.getMessage()));
		}
	}
	
}
