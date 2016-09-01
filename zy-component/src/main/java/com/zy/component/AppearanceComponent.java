package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.usr.Appearance;
import com.zy.entity.usr.User;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.AppearanceAdminVo;
import com.zy.vo.AppearanceVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppearanceComponent {
	
	@Autowired
	private CacheComponent cacheComponent;
	
	public AppearanceVo buildVo(Appearance appearance) {
		AppearanceVo appearanceVo = new AppearanceVo();
		BeanUtils.copyProperties(appearance, appearanceVo);
		
		String idCardNumber = appearanceVo.getIdCardNumber();
		appearanceVo.setIdCardNumber(StringUtils.overlay(idCardNumber, "***********", 6, idCardNumber.length()));
		
		String image1 = appearance.getImage1();
		String image2 = appearance.getImage2();
		
		appearanceVo.setImage1Thumbnail(StringUtils.isBlank(image1)? null : GcUtils.getThumbnail(image1, 120, 120));
		appearanceVo.setImage2Thumbnail(StringUtils.isBlank(image2)? null : GcUtils.getThumbnail(image2, 120, 120));
		return appearanceVo;
	}
	
	public AppearanceAdminVo buildAdminVo(Appearance appearance) {
		AppearanceAdminVo appearanceAdminVo = new AppearanceAdminVo();
		BeanUtils.copyProperties(appearance, appearanceAdminVo);
		User user = cacheComponent.getUser(appearance.getUserId());
		appearanceAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(user));
		appearanceAdminVo.setImage1Thumbnail(GcUtils.getThumbnail(appearance.getImage1(), 120, 120));
		appearanceAdminVo.setImage2Thumbnail(GcUtils.getThumbnail(appearance.getImage2(), 120, 120));
		return appearanceAdminVo;
	}
}
