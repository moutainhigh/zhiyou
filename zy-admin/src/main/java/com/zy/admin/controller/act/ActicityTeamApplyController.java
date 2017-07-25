package com.zy.admin.controller.act;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.ActivityTeamApplyComponent;
import com.zy.component.ActivityTicketComponent;
import com.zy.entity.act.*;
import com.zy.entity.usr.User;
import com.zy.model.query.ActivityQueryModel;
import com.zy.model.query.ActivityTeamApplyQueryModel;
import com.zy.model.query.ActivityTicketQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.*;
import com.zy.vo.ActivityTeamApplyAdminVo;
import com.zy.vo.ActivityTicketAdminVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@RequestMapping("/activityTeamApply")
@Controller
public class ActicityTeamApplyController {

	@Autowired
	private ActivityTeamApplyService activityTeamApplyService;

	@Autowired
	private UserService userService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private ActivityTeamApplyComponent activityTeamApplyComponent;

	@Autowired
	private ActivityTicketService activityTicketService;

	@Autowired
	private ActivityTicketComponent activityTicketComponent;

	@Autowired
	private ActivityApplyService activityApplyService;

	@Autowired
	private ActivitySignInService activitySignInService;


	@RequiresPermissions("activityApply:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "act/activityTeamApplyList";
	}


	/**
	 * 活动团队报名
	 * @param activityTeamApplyQueryModel
	 * @param activityTitleLK
	 * @param nicknameLK
	 * @param phoneEQ
     * @return
     */
	@RequiresPermissions("activityApply:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<ActivityTeamApplyAdminVo> list(ActivityTeamApplyQueryModel activityTeamApplyQueryModel, String activityTitleLK
										, String nicknameLK, String phoneEQ) {
		if (StringUtils.isNotBlank(activityTitleLK)) {
			Page<Activity> page = activityService.findPage(ActivityQueryModel.builder().titleLK(activityTitleLK).build());
			List<Activity> data = page.getData();
			if (data.isEmpty()) {
				return new Grid<ActivityTeamApplyAdminVo>(PageBuilder.empty(activityTeamApplyQueryModel.getPageSize(), activityTeamApplyQueryModel.getPageNumber()));
			}
			activityTeamApplyQueryModel.setActivityIdIN(data.stream().map(v -> v.getId()).toArray(Long[]::new));
		}

		if (StringUtils.isNotBlank(nicknameLK) || StringUtils.isNotBlank(phoneEQ)) {
			List<User> all = userService.findAll(UserQueryModel.builder().nicknameLK(nicknameLK).phoneEQ(phoneEQ).build());
			if (all.isEmpty()) {
				return new Grid<ActivityTeamApplyAdminVo>(PageBuilder.empty(activityTeamApplyQueryModel.getPageSize(), activityTeamApplyQueryModel.getPageNumber()));
			}
			activityTeamApplyQueryModel.setBuyerIdIN(all.stream().map(v -> v.getId()).toArray(Long[]::new));
		}
		Page<ActivityTeamApply> page = activityTeamApplyService.findPage(activityTeamApplyQueryModel);
		Page<ActivityTeamApplyAdminVo> voPage = PageBuilder.copyAndConvert(page, v -> activityTeamApplyComponent.buildAdminVo(v));
		return new Grid<>(voPage);
	}

	@RequiresPermissions("activityTicket:view")
	@RequestMapping(value = "/ticket",method = RequestMethod.GET)
	public String ticketList(Model model) {
		return "act/activityTicketList";
	}

	/**
	 * 活动票务
	 * @param activityTicketQueryModel
	 * @param activityTitleLK
	 * @param buyerNicknameLK
	 * @param buyerPhoneEQ
	 * @param usedPhoneEQ
     * @return
     */
	@RequiresPermissions("activityTicket:view")
	@RequestMapping(value = "/ticket", method = RequestMethod.POST)
	@ResponseBody
	public Grid<ActivityTicketAdminVo> ticketList(ActivityTicketQueryModel activityTicketQueryModel, String activityTitleLK
										, String buyerNicknameLK, String buyerPhoneEQ, String usedPhoneEQ) {
		ActivityTeamApplyQueryModel activityTeamApplyQueryModel = new ActivityTeamApplyQueryModel();
		if (StringUtils.isNotBlank(activityTitleLK)) {
			Page<Activity> page = activityService.findPage(ActivityQueryModel.builder().titleLK(activityTitleLK).build());
			List<Activity> data = page.getData();
			if (data.isEmpty()) {
				return new Grid<ActivityTicketAdminVo>(PageBuilder.empty(activityTicketQueryModel.getPageSize(), activityTicketQueryModel.getPageNumber()));
			}
			activityTeamApplyQueryModel.setActivityIdIN(data.stream().map(v -> v.getId()).toArray(Long[]::new));
		}

		if (StringUtils.isNotBlank(buyerNicknameLK) || StringUtils.isNotBlank(buyerPhoneEQ)) {
			List<User> all = userService.findAll(UserQueryModel.builder().nicknameLK(buyerNicknameLK).phoneEQ(buyerPhoneEQ).build());
			if (all.isEmpty()) {
				return new Grid<ActivityTicketAdminVo>(PageBuilder.empty(activityTicketQueryModel.getPageSize(), activityTicketQueryModel.getPageNumber()));
			}
			activityTeamApplyQueryModel.setBuyerIdIN(all.stream().map(v -> v.getId()).toArray(Long[]::new));
		}
		if (StringUtils.isNotBlank(usedPhoneEQ)) {
			User usedUser = userService.findByPhone(usedPhoneEQ);
			if (null == usedUser) {
				return new Grid<ActivityTicketAdminVo>(PageBuilder.empty(activityTicketQueryModel.getPageSize(), activityTicketQueryModel.getPageNumber()));
			}
			activityTicketQueryModel.setUserId(usedUser.getId());
		}
		List<ActivityTeamApply> activityTeamApplies = activityTeamApplyService.findAll(activityTeamApplyQueryModel);
		if (activityTeamApplies.isEmpty()) {
			return new Grid<ActivityTicketAdminVo>(PageBuilder.empty(activityTicketQueryModel.getPageSize(), activityTicketQueryModel.getPageNumber()));
		}else{
			activityTicketQueryModel.setTeamApplyIdIN(activityTeamApplies.stream().map(v -> v.getId()).toArray(Long[]::new));
		}
		Page<ActivityTicket> page = activityTicketService.findPage(activityTicketQueryModel);
		Page<ActivityTicketAdminVo> voPage = PageBuilder.copyAndConvert(page, v -> activityTicketComponent.buildAdminVo(v));
		return new Grid<>(voPage);
	}

	/**
	 * 二维码重置
	 * @param id
	 * @param redirectAttributes
     * @return
     */
	@RequiresPermissions("activityTicket:edit")
	@RequestMapping(value = "/ticketReset" , method = RequestMethod.POST)
	public String freeze(@RequestParam Long id, RedirectAttributes redirectAttributes) {
		validate(id, NOT_NULL, "activityTicket id is null");
		ActivityTicket activityTicket = activityTicketService.findOne(id);
		validate(activityTicket, NOT_NULL, "activityTicket id " + id + " not found");

		Long teamApplyId = activityTicket.getTeamApplyId();
		validate(teamApplyId, NOT_NULL, "activityTeamApply id is null");
		ActivityTeamApply activityTeamApply = activityTeamApplyService.findOne(teamApplyId);
		validate(activityTeamApply, NOT_NULL, "activityTeamApply id " + teamApplyId + " not found");

		Long userId = activityTicket.getUserId();
		validate(userId, NOT_NULL, "user id is null");
		ActivitySignIn signIn = activitySignInService.findByActivityIdAndUserId(activityTeamApply.getActivityId(), userId);
		if (signIn != null){
			redirectAttributes.addFlashAttribute(ResultBuilder.error("用票人已签到，二维码重置失败"));
			return "redirect:/activityTeamApply/ticket";
		}else {
			try {
				ActivityApply activityApply = activityApplyService.findByActivityIdAndUserId(activityTeamApply.getActivityId(), userId);
				Long activityApplyId = activityApply.getId();
				validate(activityApplyId, NOT_NULL, "activityApply id is null");
				validate(activityApply, NOT_NULL, "activityApply id " + activityApplyId + " not found");
				activityTicket.setIsUsed(0);
				activityApply.setInviterId(null);
				activityApply.setActivityApplyStatus(ActivityApply.ActivityApplyStatus.已报名);
				activityTicketService.update(activityTicket);
				activityApplyService.update(activityApply);
				redirectAttributes.addFlashAttribute(ResultBuilder.ok("二维码重置成功"));
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute(ResultBuilder.error(e.getMessage()));
			}
			return "redirect:/activityTeamApply/ticket";
		}
	}

}
