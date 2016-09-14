package com.zy.admin.controller.fnc;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.TransferComponent;
import com.zy.entity.fnc.Transfer;
import com.zy.entity.fnc.Transfer.TransferStatus;
import com.zy.entity.fnc.Transfer.TransferType;
import com.zy.model.query.TransferQueryModel;
import com.zy.service.TransferService;
import com.zy.vo.TransferAdminVo;

@RequestMapping("/transfer")
@Controller
public class TransferController {

	@Autowired
	private TransferService transferService;
	
	@Autowired
	private TransferComponent transferComponent;
	
	@RequiresPermissions("transfer:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("transferTypes", TransferType.values());
		model.addAttribute("transferStatuses", TransferStatus.values());
		return "fnc/transferList";
	}

	@RequiresPermissions("transfer:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<TransferAdminVo> list(TransferQueryModel transferQueryModel) {
		Page<Transfer> page = transferService.findPage(transferQueryModel);
		Page<TransferAdminVo> voPage = PageBuilder.copyAndConvert(page, transferComponent::buildAdminVo);
		return new Grid<>(voPage);
	}
}
