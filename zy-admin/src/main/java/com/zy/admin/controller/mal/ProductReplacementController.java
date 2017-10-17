package com.zy.admin.controller.mal;

import com.zy.admin.model.AdminPrincipal;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.ProductReplacementComponent;
import com.zy.entity.mal.ProductReplacement;
import com.zy.entity.usr.User;
import com.zy.model.dto.LogisticsDto;
import com.zy.model.query.ProductReplacementQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.ProductReplacementService;
import com.zy.service.UserService;
import com.zy.vo.ProductReplacementAdminVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/productReplacement")
@Controller
public class ProductReplacementController {

	@Autowired
	private ProductReplacementService productReplacementService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductReplacementComponent productReplacementComponent;

	@RequiresPermissions("productReplacement:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "mal/productReplacementList";
	}

	@RequiresPermissions("productReplacement:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<ProductReplacementAdminVo> list(ProductReplacementQueryModel productReplacementQueryModel, String userPhoneEQ, String userNicknameLK) {

		if (StringUtils.isNotBlank(userPhoneEQ) || StringUtils.isNotBlank(userNicknameLK)) {
			UserQueryModel userQueryModel = new UserQueryModel();
			userQueryModel.setPhoneEQ(userPhoneEQ);
			userQueryModel.setNicknameLK(userNicknameLK);
			List<User> users = userService.findAll(userQueryModel);
			Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
			productReplacementQueryModel.setUserIdIN(userIds);
		}

		Page<ProductReplacement> page = productReplacementService.findPage(productReplacementQueryModel);
		Page<ProductReplacementAdminVo> voPage = PageBuilder.copyAndConvert(page, productReplacementComponent::buildAdminVo);
		return new Grid<ProductReplacementAdminVo>(voPage);
	}

	@RequiresPermissions("productReplacement:deliver")
	@RequestMapping(value = "/deliver", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deliver(@RequestParam Long id, LogisticsDto logisticsDto , AdminPrincipal principal) {
		try {
			productReplacementService.deliver(id, logisticsDto, principal.getUserId());
			return ResultBuilder.ok("操作成功");
		} catch (Exception e) {
			return ResultBuilder.error(e.getMessage());
		}
	}

	@RequiresPermissions("productReplacement:reject")
	@RequestMapping(value = "/reject", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> reject(@RequestParam Long id, String remark, AdminPrincipal principal) {
		try {
			productReplacementService.reject(id, remark, principal.getUserId());
			return ResultBuilder.ok("操作成功");
		} catch (Exception e) {
			return ResultBuilder.error(e.getMessage());
		}
	}
}
