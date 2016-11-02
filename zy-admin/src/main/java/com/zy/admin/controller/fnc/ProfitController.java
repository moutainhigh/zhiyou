package com.zy.admin.controller.fnc;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.ProfitComponent;
import com.zy.entity.fnc.Profit;
import com.zy.entity.fnc.Profit.ProfitStatus;
import com.zy.entity.fnc.Profit.ProfitType;
import com.zy.entity.usr.User;
import com.zy.model.query.ProfitQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.ProfitService;
import com.zy.service.UserService;
import com.zy.vo.ProfitAdminVo;

@RequestMapping("/profit")
@Controller
public class ProfitController {

    @Autowired
    @Lazy
    private ProfitService profitService;

    @Autowired
    private ProfitComponent profitComponent;

    @Autowired
    private UserService userService;

    @RequiresPermissions("profit:view")
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("profitTypes", ProfitType.values());
        return "fnc/profitList";
    }

    @RequiresPermissions("profit:view")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Grid<ProfitAdminVo> list(ProfitQueryModel profitQueryModel, String phoneEQ, String nicknameLK) {

        if (StringUtils.isNotBlank(phoneEQ) || StringUtils.isNotBlank(nicknameLK)) {
            UserQueryModel userQueryModel = new UserQueryModel();
            userQueryModel.setPhoneEQ(phoneEQ);
            userQueryModel.setNicknameLK(nicknameLK);

            List<User> users = userService.findAll(userQueryModel);
            if (users == null || users.size() == 0) {
                return new Grid<ProfitAdminVo>(PageBuilder.empty(profitQueryModel.getPageSize(), profitQueryModel.getPageNumber()));
            }
            Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);
            profitQueryModel.setUserIdIN(userIds);
        }
        Page<Profit> page = profitService.findPage(profitQueryModel);
        Page<ProfitAdminVo> adminVoPage = PageBuilder.copyAndConvert(page, profitComponent::buildAdminVo);
        return new Grid<ProfitAdminVo>(adminVoPage);
    }
    
	@RequiresPermissions("profit:grant")
	@RequestMapping(value = "/grant")
	public String grant(@NotNull Long id, RedirectAttributes redirectAttributes) {
		Profit profit = profitService.findOne(id);
		validate(profit, NOT_NULL, "profit id" + id + " not found");
		validate(profit, v -> (v.getProfitStatus() == ProfitStatus.待发放), "ProfitStatus is error, profit id is " + id + "");
		try {
			profitService.grant(id);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("收益发放成功！"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error(e.getMessage()));
		}
		return "redirect:/profit";
	}
	
	@RequiresPermissions("profit:grant")
	@RequestMapping(value = "/batchGrant")
	@ResponseBody
	public Result<?> batchGrant(@NotBlank String ids) {
		if(StringUtils.isBlank(ids)){
			return ResultBuilder.error("请至少选择一条记录！");
		} else {
			String[] idStringArray = ids.split(",");
			for(String idString : idStringArray){
				Long id = new Long(idString);
				Profit profit = profitService.findOne(id);
				validate(profit, NOT_NULL, "profit id" + id + " not found");
				validate(profit, v -> (v.getProfitStatus() == ProfitStatus.待发放), "ProfitStatus is error, profit id is " + id + "");
				try {
					profitService.grant(id);
				} catch (Exception e) {
					return ResultBuilder.error(e.getMessage());
				}
			}
			return ResultBuilder.ok("收益发放成功！");
		}
	}
	
}
