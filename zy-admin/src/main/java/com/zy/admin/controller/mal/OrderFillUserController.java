package com.zy.admin.controller.mal;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.OrderFillUserComponent;
import com.zy.entity.mal.OrderFillUser;
import com.zy.entity.usr.User;
import com.zy.model.query.OrderFillUserQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.OrderFillUserService;
import com.zy.service.UserService;
import com.zy.vo.OrderFillUserAdminVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static com.zy.common.model.result.ResultBuilder.ok;
import static com.zy.model.Constants.MODEL_ATTRIBUTE_RESULT;

@RequestMapping("/orderFillUser")
@Controller
public class OrderFillUserController {

	@Autowired
	private OrderFillUserService orderFillUserService;

	@Autowired
	private UserService userService;

	@Autowired
	private OrderFillUserComponent orderFillUserComponent;

	@RequiresPermissions("orderFillUser:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "mal/orderFillUserList";
	}

	@RequiresPermissions("orderFillUser:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<OrderFillUserAdminVo> list(OrderFillUserQueryModel orderFillUserQueryModel, String userPhoneEQ, String userNicknameLK) {


		if (StringUtils.isNotBlank(userPhoneEQ) || StringUtils.isNotBlank(userNicknameLK)) {
			UserQueryModel userQueryModel = new UserQueryModel();
			userQueryModel.setPhoneEQ(userPhoneEQ);
			userQueryModel.setNicknameLK(userNicknameLK);
			List<User> users = userService.findAll(userQueryModel);
			Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
			orderFillUserQueryModel.setUserIdIN(userIds);
		}

		Page<OrderFillUser> page = orderFillUserService.findPage(orderFillUserQueryModel);
		Page<OrderFillUserAdminVo> voPage = PageBuilder.copyAndConvert(page, orderFillUserComponent::buildAdminVo);
		return new Grid<OrderFillUserAdminVo>(voPage);
	}

	@RequiresPermissions("orderFillUser:edit")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> create(String phone, String remark) {

		User user = userService.findByPhone(phone);
		if(user == null) {
			return ResultBuilder.error("手机号不存在");
		}

		OrderFillUser orderFillUser = new OrderFillUser();
		orderFillUser.setUserId(user.getId());
		orderFillUser.setRemark(remark);
		orderFillUserService.create(orderFillUser);
		return ResultBuilder.ok("操作成功");
	}

	@RequiresPermissions("orderFillUser:edit")
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(@RequestParam Long id, RedirectAttributes redirectAttributes) {
		orderFillUserService.delete(id);
		redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ok("操作成功"));
		return "redirect:/orderFillUser";
	}

	@ResponseBody
	@RequestMapping("/checkUserPhone")
	public boolean checkUserPhone(String phone) {
		User user = userService.findByPhone(phone);
		List<OrderFillUser> all = new ArrayList<>();
		if(user != null){
			all = orderFillUserService.findAll(OrderFillUserQueryModel.builder().userIdEQ(user.getId()).build());
		}
		return all.isEmpty();
	}
}
