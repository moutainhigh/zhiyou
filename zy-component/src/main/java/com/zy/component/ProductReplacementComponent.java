package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.mal.ProductReplacement;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.ProductReplacementAdminVo;
import com.zy.vo.ProductReplacementDetailVo;
import com.zy.vo.ProductReplacementListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductReplacementComponent {

	@Autowired
	private CacheComponent cacheComponent;

	static final String pattern = "yyyy-MM-dd HH:mm";

	public ProductReplacementAdminVo buildAdminVo(ProductReplacement productReplacement) {
		ProductReplacementAdminVo productReplacementAdminVo = new ProductReplacementAdminVo();
		BeanUtils.copyProperties(productReplacement, productReplacementAdminVo);

		Long userId = productReplacement.getUserId();
		if (userId != null) {
			productReplacementAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(userId)));
		}

		productReplacementAdminVo.setCreatedTimeLabel(GcUtils.formatDate(productReplacement.getCreatedTime(), pattern));
		productReplacementAdminVo.setDeliveredTimeLabel(GcUtils.formatDate(productReplacement.getDeliveredTime(), pattern));
		productReplacementAdminVo.setProductReplacementStatusStyle(GcUtils.getProductReplacementStatusStyle(productReplacement.getProductReplacementStatus()));
		return productReplacementAdminVo;
	}

	public ProductReplacementListVo buildListVo(ProductReplacement productReplacement) {
		ProductReplacementListVo productReplacementListVo = new ProductReplacementListVo();
		BeanUtils.copyProperties(productReplacement, productReplacementListVo);

		productReplacementListVo.setCreatedTimeLabel(GcUtils.formatDate(productReplacement.getCreatedTime(), pattern));
		return productReplacementListVo;
	}

	public ProductReplacementDetailVo buildDetailVo(ProductReplacement productReplacement) {
		ProductReplacementDetailVo productReplacementDetailVo = new ProductReplacementDetailVo();
		BeanUtils.copyProperties(productReplacement, productReplacementDetailVo);

		productReplacementDetailVo.setCreatedTimeLabel(GcUtils.formatDate(productReplacement.getCreatedTime(), pattern));
		productReplacementDetailVo.setDeliveredTimeLabel(GcUtils.formatDate(productReplacement.getDeliveredTime(), pattern));
		return productReplacementDetailVo;
	}
}
