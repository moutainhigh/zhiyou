package com.zy.admin.controller.usr;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.util.DateUtil;
import com.zy.component.UserTargetSalesComponent;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.entity.usr.UserTargetSales;
import com.zy.model.query.UserQueryModel;
import com.zy.model.query.UserTargetSalesQueryModel;
import com.zy.service.UserService;
import com.zy.service.UserTargetSalesService;
import com.zy.vo.UserTargetSalesVo;
import io.gd.generator.api.query.Direction;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

/**
 * Created by liang on 2017-09-29.
 */
@RequestMapping("/userTargetSales")
@Controller
public class UserTargerSalesController {

	@Autowired
	@Lazy
	private UserService userService;

	@Autowired
	private UserTargetSalesService userTargetSalesService;

	@Autowired
	private UserTargetSalesComponent userTargetSalesComponent;



	@RequiresPermissions("userTargetSales:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {return "usr/userTargetSalesList";}

	@RequiresPermissions("userTargetSales:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<UserTargetSalesVo> list(UserTargetSalesQueryModel userTargetSalesQueryModel, String nicknameLK , String phoneEQ) {
		if (StringUtils.isNotBlank(nicknameLK) || StringUtils.isNotBlank(phoneEQ)) {
			List<User> all = userService.findAll(UserQueryModel.builder().nicknameLK(nicknameLK).phoneEQ(phoneEQ).build());
			if (all.isEmpty()) {
				return new Grid<UserTargetSalesVo>(PageBuilder.empty(userTargetSalesQueryModel.getPageSize(), userTargetSalesQueryModel.getPageNumber()));
			}
			userTargetSalesQueryModel.setUserIdIN(all.stream().map(v -> v.getId()).toArray(Long[]::new));
		}
		if (userTargetSalesQueryModel.getOrderBy() == null){
			userTargetSalesQueryModel.setDirection(Direction.DESC);
			userTargetSalesQueryModel.setOrderBy("createTime");
		}
		Page<UserTargetSales> page = userTargetSalesService.findPage(userTargetSalesQueryModel);
		Page<UserTargetSalesVo> voPage = PageBuilder.copyAndConvert(page, userTargetSalesComponent::buildVo);
		return new Grid<>(voPage);
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create() {
		return "usr/userTargetSalesCreate";
	}

	/**
	 * 新增用户目标销量
	 * @param userTargetSales
	 * @param phone
	 * @param model
	 * @param redirectAttributes
     * @return
     */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(UserTargetSales userTargetSales,String phone, Model model, RedirectAttributes redirectAttributes) {
		try {
			User user = userService.findByPhone(phone);
			if(user == null){
				model.addAttribute("userTargetSales", userTargetSales);
				redirectAttributes.addFlashAttribute(ResultBuilder.error("新增失败，手机号不存在"));
				return "usr/userTargetSalesCreate";
			}
			if(user.getUserRank() == UserRank.V4 && user.getIsPresident()){
				model.addAttribute("userTargetSales", userTargetSales);
				redirectAttributes.addFlashAttribute(ResultBuilder.error("新增失败，用户不是大区总裁"));
				return "usr/userTargetSalesCreate";
			}
			Date now = DateUtil.getMonthData(new Date(),1,0);
			userTargetSales.setUserId(user.getId());
			userTargetSales.setYear(DateUtil.getYear(now));
			userTargetSales.setMonth(DateUtil.getMothNum(now));
			userTargetSales.setCreateTime(now);
			List<UserTargetSales> all = userTargetSalesService.findAll(UserTargetSalesQueryModel.builder().userIdEQ(user.getId()).yearEQ(userTargetSales.getYear()).monthEQ(userTargetSales.getMonth()).build());
			if(all != null && all.size() > 0 ){
				model.addAttribute("userTargetSales", userTargetSales);
				redirectAttributes.addFlashAttribute(ResultBuilder.error("新增失败，该用户当月已设置过目标量，如需更改，请编辑"));
				return "usr/userTargetSalesCreate";
			}
			userTargetSalesService.create(userTargetSales);
		} catch (Exception e) {
			model.addAttribute("userTargetSales", userTargetSales);
			model.addAttribute(e.getMessage());
			return "usr/userTargetSalesCreate";
		}
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("新增成功"));
		return "redirect:/userTargetSales";
	}

	@RequestMapping(value = "/update/{userTargetSalesId}", method = RequestMethod.GET)
	public String update(@PathVariable Long userTargetSalesId, Model model) {
		validate(userTargetSalesId, NOT_NULL, "userTargetSales id is null");
		UserTargetSales userTargetSales = userTargetSalesService.findOne(userTargetSalesId);
		model.addAttribute("userTargetSales", userTargetSales);
		return "usr/userTargetSalesUpdate";
	}

	/**
	 * 修改用户目标销量
	 * @param userTargetSales
	 * @param redirectAttributes
     * @return
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(UserTargetSales userTargetSales, RedirectAttributes redirectAttributes) {
		Long userTargetSalesId = userTargetSales.getId();
		try {
			userTargetSalesService.modifySales(userTargetSalesId,userTargetSales.getTargetCount());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error(e.getMessage()));
			return "redirect:/userTargetSales/update/" + userTargetSalesId;
		}
		return "redirect:/userTargetSales";
	}


}
