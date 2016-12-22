package com.zy.admin.controller.sys;

import com.zy.admin.model.AdminPrincipal;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.entity.sys.Setting;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.service.SettingService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.zy.common.util.ValidateUtils.validate;

@RequiresPermissions("setting:*")
@RequestMapping("/setting")
@Controller
public class SettingController {

	@Autowired
	private SettingService settingService;

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Model model) {
		Setting setting = settingService.find();
		model.addAttribute("setting", setting);
//		model.addAttribute("show", getPrincipalUserId().equals(SETTING_SUPER_ADMIN_ID) ? true : false);
		return "sys/settingEdit";
	}

	@RequestMapping(value = "/edit/base", method = RequestMethod.POST)
	public String edit(Setting setting, RedirectAttributes redirectAttributes) {
		String[] fields = new String[]{"isDev", "isWithdrawOn", "isOpenOrderFill", "orderFillTime"};
		try {
			validate(setting, fields);
			settingService.merge(setting, fields);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error(e.getMessage()));
		}
		return "redirect:/setting/edit";
	}

	@RequestMapping(value = "/checkWithdrawFeeRateScript", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> checkWithdrawFeeRateScript(String withdrawFeeRateScript) {
//		if (!getPrincipalUserId().equals(SETTING_SUPER_ADMIN_ID)) {
//			return ResultBuilder.error("权限不足");
//		}
		try {
			return ResultBuilder.result(settingService.checkScript(withdrawFeeRateScript, "withdrawFeeRateScript"));
		} catch (Exception e) {
			return ResultBuilder.error(e.getMessage());
		}
	}

	@RequestMapping(value = "/edit/withdrawFeeRateScript", method = RequestMethod.POST)
	public String withdrawFeeRateScript(String withdrawFeeRateScript, RedirectAttributes redirectAttributes) {
//		if (!getPrincipalUserId().equals(SETTING_SUPER_ADMIN_ID)) {
//			throw new UnauthorizedException();
//		}
		Result<?> result = checkWithdrawFeeRateScript(withdrawFeeRateScript);
		if (result.getCode() == BizCode.OK) {
			Setting setting = new Setting();
			setting.setWithdrawFeeRateScript(withdrawFeeRateScript);
			validate(setting, "withdrawFeeRateScript");
			settingService.merge(setting, "withdrawFeeRateScript");
		} else {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, result);
		}
		return "redirect:/setting/edit";
	}

	private Long getPrincipalUserId() {
		AdminPrincipal principal = (AdminPrincipal) SecurityUtils.getSubject().getPrincipal();
		return principal.getUserId();
	}
}
