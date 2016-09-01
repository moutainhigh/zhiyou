package com.zy.component;

import com.zy.entity.cms.Banner;
import com.zy.util.GcUtils;
import com.zy.vo.BannerAdminVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class BannerComponent {

	public BannerAdminVo buildAdminVo(Banner banner) {
		BannerAdminVo bannerAdminVo = new BannerAdminVo();
		BeanUtils.copyProperties(banner, bannerAdminVo);
		Banner.BannerPosition bannerPosition = banner.getBannerPosition();
		if (bannerPosition != null) {
			bannerAdminVo.setHeight(bannerPosition.getHeight());
			bannerAdminVo.setWidth(bannerPosition.getWidth());
		}
		bannerAdminVo.setImageThumbnail(GcUtils.getThumbnail(banner.getImage(), 240, 150));
		return bannerAdminVo;
	}
	
}
