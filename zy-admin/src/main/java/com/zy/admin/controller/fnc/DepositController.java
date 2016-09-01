package com.zy.admin.controller.fnc;


import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.support.file.ExportHandler;
import com.zy.component.DepositComponent;
import com.zy.entity.fnc.Deposit;
import com.zy.entity.fnc.PayType;
import com.zy.entity.usr.User;
import com.zy.model.query.DepositQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.DepositService;
import com.zy.service.UserService;
import com.zy.vo.DepositAdminVo;

@RequestMapping("/deposit")
@Controller
public class DepositController {

	@Autowired
	@Lazy
	private DepositService depositService;
	
	@Autowired
	private DepositComponent depositComponent;
	
	@Autowired
	private UserService userService;

	@Autowired
	@Qualifier("depositExport")
	private ExportHandler depositExport;
	
	@RequiresPermissions("deposit:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("payTypes", Arrays.asList(PayType.values()));
		return "fnc/depositList";
	}

	@RequiresPermissions("deposit:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<DepositAdminVo> list(DepositQueryModel depositQueryModel, String phoneEQ, String nicknameLK) {

		if(StringUtils.isNotBlank(phoneEQ) || StringUtils.isNotBlank(nicknameLK)) {
			UserQueryModel userQueryModel = new UserQueryModel();
			userQueryModel.setPhoneEQ(phoneEQ);
			userQueryModel.setNicknameLK(nicknameLK);
			List<User> users = userService.findAll(userQueryModel);
			if(users.isEmpty()) {
				return new Grid<DepositAdminVo>(PageBuilder.empty(depositQueryModel.getPageSize(), depositQueryModel.getPageNumber()));
			}
			Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
			depositQueryModel.setUserIdIN(userIds);
		}
		Page<Deposit> page = depositService.findPage(depositQueryModel);
		Page<DepositAdminVo> voPage = PageBuilder.copyAndConvert(page, depositComponent::buildAdminVo);
		
		return new Grid<DepositAdminVo>(voPage);
	}
	
	@RequiresPermissions("deposit:export")
	@RequestMapping("/depositExport")
	public String depositExport(DepositQueryModel depositQueryModel, String userPhoneEQ, 
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		/*List<DepositAdminVo> depositAdminVos = new ArrayList<DepositAdminVo>();
		if(StringUtils.isNotBlank(userPhoneEQ)) {
			User user = userService.findByPhone(userPhoneEQ);
			if(user != null) {
				depositQueryModel.setUserIdEQ(user.getId());
			}else {
				depositQueryModel.setUserIdEQ(0L);
			}
		}
		depositQueryModel.setPageSize(null);
		depositAdminVos = depositComponent.buildListVo(depositService.findAll(depositQueryModel));
		List<Map<String, Object>> dataList = new ArrayList<>();
		depositAdminVos.stream().forEach((depositAdminVo) -> {
			Map<String, Object> map = new HashMap<>();
			map.put("sn", depositAdminVo.getSn());
			map.put("title", depositAdminVo.getTitle());
			map.put("payType", depositAdminVo.getPayType().toString());
			map.put("depositStatus", depositAdminVo.getDepositStatus().toString());
			map.put("userNickname", depositAdminVo.getUserNickname());
			map.put("userPhone", depositAdminVo.getUserPhone());
			map.put("currencyType1", depositAdminVo.getCurrencyType1().toString());
			map.put("amount1", depositAdminVo.getAmount1());
			map.put("currencyType2", depositAdminVo.getCurrencyType2().toString());
			map.put("amount2", depositAdminVo.getAmount2());
			map.put("createdTime", depositAdminVo.getCreatedTime());
			map.put("paidTime", depositAdminVo.getPaidTime());
			map.put("outerSn", depositAdminVo.getOuterSn());
			dataList.add(map);
		});
		
		String fileName = "充值数据.xlsx";
		WebUtils.setFileDownloadHeader(response, fileName);
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			depositExport.handleExport(os, fileName, dataList);
		} catch (Exception e) {
			return null;
		}*/
		return null;
	}
	
}
