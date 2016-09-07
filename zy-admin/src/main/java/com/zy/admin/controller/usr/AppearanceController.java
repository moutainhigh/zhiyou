package com.zy.admin.controller.usr;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.AppearanceComponent;
import com.zy.entity.usr.Appearance;
import com.zy.model.query.AppearanceQueryModel;
import com.zy.service.AppearanceService;
import com.zy.vo.AppearanceAdminVo;

@RequestMapping("/appearance")
@Controller
public class AppearanceController {

	@Autowired
	private AppearanceService appearanceService;
	
	@Autowired
	private AppearanceComponent appearanceComponent;
	
	@RequiresPermissions("appearance:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "usr/appearanceList";
	}
	
	@RequiresPermissions("appearance:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<AppearanceAdminVo> list(AppearanceQueryModel appearanceQueryModel) {
		Page<Appearance> page = appearanceService.findPage(appearanceQueryModel);
		Page<AppearanceAdminVo> voPage = PageBuilder.copyAndConvert(page, appearanceComponent::buildAdminVo);
		return new Grid<>(voPage);
	}
	
	@RequiresPermissions("appearance:confirm")
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> confirm(@RequestParam Long id, @RequestParam boolean isSuccess, String confirmRemark) {
		try{
			appearanceService.confirm(id, isSuccess, confirmRemark);
		}catch (Exception e) {
			return ResultBuilder.error("审核失败:" + e.getMessage());
		}
		return ResultBuilder.ok("操作成功");
	}
}
