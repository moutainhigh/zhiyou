package com.zy.mobile.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zy.common.support.cache.CacheSupport;
import com.zy.entity.cms.Banner;
import com.zy.entity.cms.Banner.BannerPosition;
import com.zy.model.Constants;
import com.zy.model.query.BannerQueryModel;
import com.zy.service.BannerService;

@RequestMapping
@Controller
public class IndexController {

	final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private BannerService bannerService;
	
	@Autowired
	protected CacheSupport cacheSupport;
	
	@RequestMapping
	public String index(Model model) {
		/* banner */
		BannerPosition bannerPosition = BannerPosition.首页顶部;
		List<Banner> banners = cacheSupport.get(Constants.CACHE_NAME_BANNER, bannerPosition.toString());
		if(banners == null ) {
			BannerQueryModel bannerQueryModel = new BannerQueryModel();
			bannerQueryModel.setBannerPositionEQ(bannerPosition);
			bannerQueryModel.setIsReleasedEQ(true);
			banners = bannerService.findAll(bannerQueryModel);
		}
		cacheSupport.set(Constants.CACHE_NAME_BANNER, bannerPosition.toString(), banners);
		model.addAttribute("banners", banners);
		
		return "index";
	}
	
	@RequestMapping("/task")
	public String task(Model model) {
		
		return "task";
	}
	
	@RequestMapping("/html")
	public String html(String path) {
		return path;
	}
	
}
