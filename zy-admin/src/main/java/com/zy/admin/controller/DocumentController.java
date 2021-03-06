package com.zy.admin.controller;

import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.support.AliyunOssSupport;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.util.GcUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/document")
public class DocumentController {

	final Logger logger = LoggerFactory.getLogger(DocumentController.class);

	@Autowired
	private AliyunOssSupport aliyunOssSupport;

	@ResponseBody
	@RequestMapping("/upload")
	public Result<?> upload(MultipartFile file, HttpServletRequest request, Model model, Principal principal) {
		try {
			GcUtils.checkDocument(file);
			String documentUrl = aliyunOssSupport.putPublicObject(Constants.ALIYUN_BUCKET_NAME_IMAGE, "image/", file, Constants.ALIYUN_URL_IMAGE);// 上传阿里云OSS
			return new ResultBuilder<>().data(documentUrl).build();
		} catch (Exception e) {
			logger.error("上传文档失败", e);
			return ResultBuilder.error(StringEscapeUtils.escapeXml(e.getMessage()));
		}
	}
}
