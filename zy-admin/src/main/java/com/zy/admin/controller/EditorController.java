package com.zy.admin.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zy.common.support.AliyunOssSupport;
import com.zy.common.util.Encodes;
import com.zy.common.util.JsonUtils;
import com.zy.model.Constants;

@SuppressWarnings("unchecked")
@RequestMapping("/editor")
@Controller
public class EditorController {
	
	private static  Map<String, Object> config;
	
	@Autowired
	private AliyunOssSupport aliyunOssSupport;
	
	private ServletFileUpload servletFileUpload;
	
	static {
		Resource resource = new ClassPathResource("editor_config.json");
		try {
			config = JsonUtils.fromJson(resource.getInputStream(), Map.class);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("init error .please check editor_config.json");
			System.exit(1);
		}
	}
	
	public EditorController() {
		servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
		servletFileUpload.setHeaderEncoding("UTF-8");
	}

	@ResponseBody
	@RequestMapping(params = "action=config")
	public Map<String, Object> config() {
		return config;
	}
	
	@ResponseBody
	@RequestMapping(params = "action=uploadimage")
	public Map<String, Object> uploadimage(MultipartFile upfile) {
		Map<String, Object> result = new HashMap<>();
		try {
			String image = aliyunOssSupport.putPublicObject(Constants.ALIYUN_BUCKET_NAME_IMAGE, "editor/", upfile, Constants.ALIYUN_URL_IMAGE);
			result.put("state", "SUCCESS");
			result.put("title", upfile.getOriginalFilename());
			result.put("original", upfile.getOriginalFilename());
			result.put("url", image);
		} catch (Exception e) {
			result.put("state", "上传错误, " + e.getMessage());
		}
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping(params = "action=uploadfile")
	public Map<String, Object> uploadfile(MultipartFile upfile) {
		Map<String, Object> result = new HashMap<>();
		try {
			String image = aliyunOssSupport.putPublicObject(Constants.ALIYUN_BUCKET_NAME_IMAGE, "editor/", upfile, Constants.ALIYUN_URL_IMAGE);
			result.put("state", "SUCCESS");
			result.put("title", upfile.getOriginalFilename());
			result.put("original", upfile.getOriginalFilename());
			result.put("url", image);
		} catch (Exception e) {
			result.put("state", "上传错误, " + e.getMessage());
		}
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping(params = "action=uploadscrawl")
	public Map<String, Object> uploadscrawl(String upfile) {
		Map<String, Object> result = new HashMap<>();
		try {
			byte[] bytes = Encodes.decodeBase64(upfile);
			String image = aliyunOssSupport.putPublicObject(Constants.ALIYUN_BUCKET_NAME_IMAGE, "editor/", new ByteArrayInputStream(bytes), bytes.length, "image/jpg", Constants.ALIYUN_URL_IMAGE);
			result.put("state", "SUCCESS");
			result.put("title", "scrawl.jpg");
			result.put("original", "scrawl.jpg");
			result.put("url", image);
		} catch (Exception e) {
			result.put("state", "上传错误, " + e.getMessage());
		}
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping(params = "action=catchimage")
	public Map<String, Object> catchimage(@RequestParam("source[]") String[] source) {
		Map<String, Object> result = new HashMap<>();
		// TODO 暂时不实现
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping(params = "action=listimage")
	public Map<String, Object> listimage(int start, int size) {
		Map<String, Object> result = new HashMap<>();
		// TODO 暂时不实现
		return result;
	}
	
	@ResponseBody
	@RequestMapping(params = "action=listfile")
	public Map<String, Object> listfile() {
		Map<String, Object> result = new HashMap<>();
		// TODO 暂时不实现
		return result;
	}
	
	
	@ResponseBody
	@RequestMapping(params = "action=uploadsnapscreen")
	public Map<String, Object> uploadsnapscreen(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		try {
			if (!ServletFileUpload.isMultipartContent(request)) {
				throw new Exception("不是multipart");
			}
			FileItemIterator iterator = servletFileUpload.getItemIterator(request);
			FileItemStream fileItemStream = null;
			while (iterator.hasNext()) {
				fileItemStream = iterator.next();
				if (!fileItemStream.isFormField()) {
						break;
				}
			}
			if (fileItemStream == null) {
				throw new Exception("文件錯誤");
			}
			String fileName = fileItemStream.getName();
			String contentType = fileItemStream.getContentType();
			String fieldName = fileItemStream.getFieldName();
			if(!"upfile".equals(fieldName)) {
				throw new Exception("不參數錯誤");
			}
			InputStream inputStream = fileItemStream.openStream();
			byte[] bytes = IOUtils.toByteArray(inputStream);
			String image = aliyunOssSupport.putPublicObject(Constants.ALIYUN_BUCKET_NAME_IMAGE, "editor/", new ByteArrayInputStream(bytes), bytes.length, contentType, Constants.ALIYUN_URL_IMAGE);
			result.put("state", "SUCCESS");
			result.put("title", fileName);
			result.put("original", fileName);
			result.put("url", image);
		} catch (Exception e) {
			result.put("state", "上传错误, " + e.getMessage());
		}
		return result;
	}
	
}
