package com.zy.admin.controller.usr;

import com.zy.admin.model.AdminPrincipal;
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
import org.springframework.beans.factory.annotation.Autowired;
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
	private UserService userService;

	@Autowired
	private UserTargetSalesService userTargetSalesService;

	@Autowired
	private UserTargetSalesComponent userTargetSalesComponent;

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {return "usr/userTargetSalesList";}

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
	public String create(UserTargetSales userTargetSales, String phone, Model model, RedirectAttributes redirectAttributes, AdminPrincipal adminPrincipal) {
		try {
			User user = userService.findByPhone(phone);
			if(user == null){
				redirectAttributes.addFlashAttribute(ResultBuilder.error("新增失败，手机号不存在"));
				return "redirect:/userTargetSales/create";
			}
			if(user.getUserRank() != UserRank.V4 || (user.getIsPresident() == null || !user.getIsPresident())){
				redirectAttributes.addFlashAttribute(ResultBuilder.error("新增失败，用户不是大区总裁"));
				return "redirect:/userTargetSales/create";
			}
			Date now = DateUtil.getMonthData(new Date(),1,0);
			userTargetSales.setUserId(user.getId());
			userTargetSales.setYear(DateUtil.getYear(now));
			userTargetSales.setMonth(DateUtil.getMothNum(now));
			userTargetSales.setCreateTime(new Date());
			userTargetSales.setCreateId(adminPrincipal.getUserId());
			userTargetSales.setStatus(1);
			List<UserTargetSales> all = userTargetSalesService.findAll(UserTargetSalesQueryModel.builder().userIdEQ(user.getId()).yearEQ(userTargetSales.getYear()).monthEQ(userTargetSales.getMonth()).build());
			if(all != null && all.size() > 0 ){
				redirectAttributes.addFlashAttribute(ResultBuilder.error("新增失败，该用户当月已设置过目标量，如需更改，请编辑"));
				return "redirect:/userTargetSales/create";
			}
			userTargetSalesService.create(userTargetSales);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("新增失败, 原因" + e.getMessage()));
			return "redirect:/userTargetSales/create";
		}
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("新增成功"));
		return "redirect:/userTargetSales";
	}

	@RequestMapping(value = "/update/{userTargetSalesId}", method = RequestMethod.GET)
	public String update(@PathVariable Long userTargetSalesId, Model model) {
		validate(userTargetSalesId, NOT_NULL, "userTargetSales id is null");
		UserTargetSales userTargetSales = userTargetSalesService.findOne(userTargetSalesId);
		model.addAttribute("userTargetSales", userTargetSalesComponent.buildVo(userTargetSales));
		return "usr/userTargetSalesUpdate";
	}

	/**
	 * 修改用户目标销量
	 * @param userTargetSales
	 * @param redirectAttributes
     * @return
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(UserTargetSales userTargetSales, RedirectAttributes redirectAttributes, AdminPrincipal adminPrincipal) {
		Long userTargetSalesId = userTargetSales.getId();
		try {
			userTargetSales.setUpdateId(adminPrincipal.getUserId());
			userTargetSales.setUpdateTime(new Date());
			userTargetSalesService.modifySales(userTargetSalesId,userTargetSales.getTargetCount());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error(e.getMessage()));
			return "redirect:/userTargetSales/update/" + userTargetSalesId;
		}
		return "redirect:/userTargetSales";
	}

	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model, AdminPrincipal adminPrincipal) {
		if (id == null) {
			model.addAttribute(ResultBuilder.error("id为空"));
			return "usr/userTargetSalesList";
		}
		UserTargetSales targetSales = userTargetSalesService.findOne(id);
		if (targetSales == null) {
			model.addAttribute(ResultBuilder.error("没有查询到数据，id = " + id));
			return "usr/userTargetSalesList";
		}
		try {

			userTargetSalesService.delete(id, adminPrincipal.getUserId());
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("删除成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("删除失败,原因：" + e.getMessage()));
		}
		return "redirect:/userTargetSales";
	}


}
