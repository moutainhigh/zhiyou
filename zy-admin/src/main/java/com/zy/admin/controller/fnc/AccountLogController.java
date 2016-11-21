package com.zy.admin.controller.fnc;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.support.file.ExportHandler;
import com.zy.component.AccountLogComponent;
import com.zy.entity.fnc.AccountLog;
import com.zy.entity.usr.User;
import com.zy.model.query.AccountLogQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.AccountLogService;
import com.zy.service.UserService;
import com.zy.vo.AccountLogAdminVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RequestMapping("/accountLog")
@Controller
public class AccountLogController {
	
	@Autowired
	private AccountLogService accountLogService;
	
	@Autowired
	private AccountLogComponent accountLogComponent;
	
	@Autowired
	private UserService userService;

	@Autowired
	@Qualifier("accountLogExport")
	private ExportHandler accountLogExport;
	
	@RequiresPermissions("accountLog:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(String phoneEQ, Boolean fromParent, Model model) {
		model.addAttribute("phone", phoneEQ);
		model.addAttribute("fromParent", fromParent);
		return "fnc/accountLogList";
	}
	
	@RequiresPermissions("accountLog:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<AccountLogAdminVo> list(AccountLogQueryModel accountLogQueryModel, String phoneEQ, String nicknameLK) {
		
		if(StringUtils.isNotBlank(phoneEQ) || StringUtils.isNotBlank(nicknameLK)) {
			List<User> users = userService.findAll(UserQueryModel.builder().phoneEQ(phoneEQ).nicknameLK(nicknameLK).build());
			if(users == null || users.size() == 0) {
				return new Grid<AccountLogAdminVo>(PageBuilder.empty(accountLogQueryModel.getPageSize(), accountLogQueryModel.getPageNumber()));
			}
			Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
			accountLogQueryModel.setUserIdIN(userIds);
		}
		
		Page<AccountLog> page = accountLogService.findPage(accountLogQueryModel);
		Page<AccountLogAdminVo> voPage = PageBuilder.copyAndConvert(page, accountLogComponent::buildAdminVo);
		return new Grid<AccountLogAdminVo>(voPage);
	}

	@RequiresPermissions("accountLog:export")
	@RequestMapping("/accountLogExport")
	public String transactionExport(AccountLogQueryModel transactionQueryModel, String userPhoneEQ, 
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		/*List<TransactionVo> transactionVos = new ArrayList<TransactionVo>();
		if(StringUtils.isNotBlank(userPhoneEQ)) {
			User user = userService.findByPhone(userPhoneEQ);
			if(user != null) {
				transactionQueryModel.setUserIdEQ(user.getId());
			}else {
				transactionQueryModel.setUserIdEQ(0L);
			}
		}
		transactionQueryModel.setPageSize(null);
		transactionVos = transactionComponent.buildListVo(transactionService.findAll(transactionQueryModel), true);
		List<Map<String, Object>> dataList = new ArrayList<>();
		transactionVos.stream().forEach((transactionVo) -> {
			Map<String, Object> map = new HashMap<>();
			map.put("id", transactionVo.getId());
			map.put("userUserType", transactionVo.getUserUserType().toString());
			map.put("userNickname", transactionVo.getUserNickname());
			map.put("userPhone", transactionVo.getUserPhone());
			map.put("title", transactionVo.getTitle());
			map.put("transactionRefType", transactionVo.getTransactionRefType().toString());
			map.put("refSn", transactionVo.getRefSn());
			map.put("transTime", transactionVo.getTransTime());
			map.put("beforeMoney", transactionVo.getBeforeMoney());
			map.put("beforePoint", transactionVo.getBeforePoint());
			map.put("transMoney", transactionVo.getTransMoney());
			map.put("transPoint", transactionVo.getTransPoint());
			map.put("afterMoney", transactionVo.getAfterMoney());
			map.put("afterPoint", transactionVo.getAfterPoint());
			map.put("transactionType", transactionVo.getTransactionType().toString());
			map.put("isOuter", transactionVo.getIsOuter()?"是":"否");
			map.put("outerFee", transactionVo.getOuterFee());
			map.put("outerRemark", transactionVo.getOuterRemark());
			dataList.add(map);
		});
		
		String fileName = "流水管理.xlsx";
		WebUtils.setFileDownloadHeader(response, fileName);
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			transactionExport.handleExport(os, fileName, dataList);
		} catch (Exception e) {
			return null;
		}*/
		return null;
	}
}
