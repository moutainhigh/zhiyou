package com.zy.mobile.controller.ucenter;

import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.exception.UnauthenticatedException;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.*;
import com.zy.entity.act.*;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.PayType;
import com.zy.entity.fnc.Payment;
import com.zy.entity.usr.User;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.ActivityApplyQueryModel;
import com.zy.model.query.ActivityCollectQueryModel;
import com.zy.model.query.ActivityTeamApplyQueryModel;
import com.zy.model.query.ActivityTicketQueryModel;
import com.zy.service.*;
import com.zy.vo.ActivityListVo;
import com.zy.vo.ActivityTeamApplyAdminVo;
import com.zy.vo.ActivityTeamApplyListVo;
import com.zy.vo.ActivityTicketListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@RequestMapping("/u/activity")
@Controller
public class UcenterActivityController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private ActivityCollectService activityCollectService;
	
	@Autowired
	private ActivityApplyService activityApplyService;

	@Autowired
	private ActivityTeamApplyService activityTeamApplyService;

	@Autowired
	private ActivitySignInService activitySignInService;

	@Autowired
	private UserComponent userComponent;
	
	@Autowired
	private ActivityComponent activityComponent;

	@Autowired
	private ActivityApplyComponent activityApplyComponent;

    @Autowired
    private ActivityTeamApplyComponent activityTeamApplyComponent;

	@Autowired
	private CacheComponent cacheComponent;

	@Autowired
	private ActivityTicketService activityTicketService;

	@Autowired
	private ActivityTicketComponent activityTicketComponent;

	/**
	 * 报名后跳转到选择支付方式页面
	 * @param id
	 * @param principal
	 * @param model
	 * @param redirectAttributes
	 * @param request
     * @return
     */
	@RequestMapping(value = "/apply")
	public String apply(Long id, Principal principal, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		try {
			Long a = querySurplus(id);
			if (a > 0){
				activityService.apply(id, principal.getUserId(), null);
				redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("报名成功,请点击付费完成报名"));
				return "redirect:/u/activity/" + id + "/activityApply";
			}else {
				redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("报名失败,票不足"));
				return "redirect:/activity/" + id;
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("报名异常," + e.getMessage()));
			return "redirect:/activity/" + id;
		}
	}

	@RequestMapping(value = "/payer")
	public String payer(Principal principal, Model model) {
		List<ActivityApply> all = activityApplyService.findAll(ActivityApplyQueryModel.builder().payerUserIdEQ(principal.getUserId()).build());
		if (!all.isEmpty()) {
			model.addAttribute("activityApplyVos", all.stream().map(activityApplyComponent::buildListVo).collect(Collectors.toList()));
		}
		return "ucenter/activity/payerList";
	}

	@RequestMapping(path = "/{activityId}/activityApply", method = RequestMethod.GET)
	public String activityApply1(@PathVariable Long activityId, Model model, Principal principal) {

		Activity activity = activityService.findOne(activityId);
		validate(activity, NOT_NULL, "activity id " + activityId + " not found");

		ActivityApply activityApply = activityApplyService.findByActivityIdAndUserId(activityId, principal.getUserId());
		validate(activityApply, NOT_NULL, "activity apply id" + activityId + " not found");
		if(!activityApply.getUserId().equals(principal.getUserId())){
			throw new BizException(BizCode.ERROR, "非自己的订单, 不能操作");
		}
		if(activityApply.getActivityApplyStatus() != ActivityApply.ActivityApplyStatus.已报名){
			return "redirect:/activity/" + activityId;
		}

		model.addAttribute("title", activity.getTitle());
		model.addAttribute("activityApplyId", activityApply.getId());
		model.addAttribute("amount", activityApply.getAmount());
		return "ucenter/activity/activityApply1";
	}

	/**
	 * 团队报名后跳转到选择支付方式页面
	 * @param
	 * @param principal
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/applyTeam")
	public String applyTeam(Long activityId, Long count, BigDecimal amount, Principal principal, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		try {
			Long a = querySurplus(activityId);
			ActivityTeamApply  activityTeamApply = new ActivityTeamApply();
			if (a >= count){
				activityTeamApply.setActivityId(activityId);
				activityTeamApply.setBuyerId(principal.getUserId());
				activityTeamApply.setCreateTime(new Date());
				activityTeamApply.setPaidStatus(ActivityTeamApply.PaidStatus.未支付);
				activityTeamApply.setCount(count);
				activityTeamApply.setAmount(amount);
                Long id = activityTeamApplyService.insert(activityTeamApply);
				redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("报名成功,请点击付费完成报名"));
				return "redirect:/u/activity/" + id + "/activityTeamApply";
			}else {
				redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("票量不足,剩余"+ a + "张，请重新选折"));
				return "redirect:/activity/" + activityId;
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("报名异常," + e.getMessage()));
			return "redirect:/activity/" + activityId;
		}
	}

	private Long querySurplus(Long activityId) {
		Long id = activityId;
		Long number = activityTeamApplyService.findPayNumber(activityId);
		Long count = activityApplyService.queryCount(activityId);
		Activity activity = activityService.findOne(id);
		return activity.getMaxCount() - number - count;
	}

	@RequestMapping(path = "/{id}/activityTeamApply", method = RequestMethod.GET)
	public String activityTeamApply(@PathVariable Long id, Model model, Principal principal) {
		ActivityTeamApply activityTeamApply = activityTeamApplyService.findOne(id);
		Activity activity = activityService.findOne(activityTeamApply.getActivityId());
		validate(activityTeamApply, NOT_NULL, "activity id " + id + " not found");

		model.addAttribute("title", activity.getTitle());
		model.addAttribute("activityApplyId", activityTeamApply.getId());
		model.addAttribute("amount", activityTeamApply.getAmount());
		return "ucenter/activity/activityApply2";
	}

	/**
	 * 团队跳转到支付类型页面
	 * @param activityApplyId
	 * @param payerPhone
	 * @param model
	 * @param redirectAttributes
	 * @param principal
     * @return
     */
	@RequestMapping(path = "/activityApply2", method = RequestMethod.POST)
	public String activityApply2(@RequestParam Long activityApplyId, String payerPhone, Model model, RedirectAttributes redirectAttributes,
								 Principal principal) {

		ActivityTeamApply activityTeamApply = activityTeamApplyService.findOne(activityApplyId);
		validate(activityTeamApply, NOT_NULL, "activity apply " + activityApplyId + " id not found");

		Long activityId = activityTeamApply.getActivityId();
		Activity activity = activityService.findOne(activityId);
		if (activityTeamApply.getPaidStatus()== ActivityTeamApply.PaidStatus.已支付) {
			return "redirect:/activity/" + activityId;
		}
		Long userId = activityTeamApply.getBuyerId();
		// 自己支付
		if(!userId.equals(principal.getUserId())){
			throw new BizException(BizCode.ERROR, "非自己的订单不能操作");
		}
		model.addAttribute("title", activity.getTitle());
		model.addAttribute("activityTeamApplyId", activityTeamApply.getId());
		model.addAttribute("amount", activityTeamApply.getAmount());
		return "ucenter/pay/activityTeamPay";
	}


	/**
	 * 跳转到支付类型页面
	 * @param activityApplyId
	 * @param payerPhone
	 * @param model
	 * @param redirectAttributes
	 * @param principal
     * @return
     */
	@RequestMapping(path = "/activityApply", method = RequestMethod.POST)
	public String activityApply1(@RequestParam Long activityApplyId, String payerPhone, Model model, RedirectAttributes redirectAttributes,
	                             Principal principal) {

		ActivityApply activityApply = activityApplyService.findOne(activityApplyId);
		validate(activityApply, NOT_NULL, "activity apply " + activityApplyId + " id not found");

		Activity activity = activityService.findOne(activityApply.getActivityId());
		Long activityId = activity.getId();
		if (activityApply.getActivityApplyStatus() == ActivityApply.ActivityApplyStatus.已支付) {
			return "redirect:/activity/" + activityId;
		}
		if (activityApply.getPayerUserId() != null) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("请等待他人付款"));
			return "redirect:/activity/" + activityId;
		}

		//选择他人代付
		Long userId = activityApply.getUserId();
		if (StringUtils.isNotBlank(payerPhone)) {
			User payer = userService.findByPhone(payerPhone);
			if (payer == null) {
				redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("手机号不存在"));
				return "redirect:/u/activity/" + activityId + "/activityApply";
			}
			if (payer.getUserType() != User.UserType.代理) {
				redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("待付人必须是代理"));
				return "redirect:/u/activity/" + activityId + "/activityApply";
			}
			Long payerId = payer.getId();
			if (userId.equals(payerId)) {
				redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("待付人不能是自己"));
				return "redirect:/u/activity/" + activityId + "/activityApply";
			}

			activityApply.setPayerUserId(payerId);
			activityApplyService.modifyPayerUserId(activityApply.getId(), payerId);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("请等待他人付款"));
			return "redirect:/activity/" + activityId;
		}

		// 自己支付
		if(!userId.equals(principal.getUserId())){
			throw new BizException(BizCode.ERROR, "非自己的订单不能操作");
		}
		model.addAttribute("title", activity.getTitle());
		model.addAttribute("activityApplyId", activityApply.getId());
		model.addAttribute("amount", activityApply.getAmount());
		return "ucenter/pay/activityPay";
	}

	//他人代付 去支付
	@RequestMapping(path = "/activityApply/{activityApplyId}/payer", method = RequestMethod.GET)
	public String payerPay(@PathVariable Long activityApplyId, Principal principal, Model model, RedirectAttributes redirectAttributes) {

		ActivityApply activityApply = activityApplyService.findOne(activityApplyId);
		validate(activityApply, NOT_NULL, "activity apply " + activityApplyId + " id not found");

		Activity activity = activityService.findOne(activityApply.getActivityId());
		Long activityId = activity.getId();
		if (activityApply.getActivityApplyStatus() == ActivityApply.ActivityApplyStatus.已支付) {
			return "redirect:/activity/" + activityId;
		}
		if (activityApply.getPayerUserId() == null) {
			throw new UnauthenticatedException("权限不足");
		}
		if (!principal.getUserId().equals(activityApply.getPayerUserId())) {
			throw new UnauthenticatedException("权限不足");
		}
		model.addAttribute("title", activity.getTitle());
		model.addAttribute("activityApplyId", activityApply.getId());
		model.addAttribute("amount", activityApply.getAmount());
		return "ucenter/pay/activityPay";
	}


	@RequestMapping(path = "/{id}/ticketList", method = RequestMethod.GET)
	public String ticketList(@PathVariable Long id, Model model, Principal principal) {
		ActivityTeamApply activityTeamApply = activityTeamApplyService.findOne(id);
		validate(activityTeamApply, NOT_NULL, "activityTeamApply id " + id + " not found");
		ActivityTicketQueryModel activityTicketQueryModel = new ActivityTicketQueryModel();
		activityTicketQueryModel.setTeamApplyId(id);
		List<ActivityTicket> activityTickets = activityTicketService.findAll(activityTicketQueryModel);
		List<ActivityTicketListVo> list = activityTickets.stream().map(v -> {
			return activityTicketComponent.buildListVo(v);
		}).collect(Collectors.toList());
		model.addAttribute("activityTickets", list);
		return "ucenter/activity/ticketList";
	}

	@RequestMapping("/applyList")
	public String applyList(Principal principal, Model model) {
		
		List<ActivityApply> list = activityApplyService.findAll(ActivityApplyQueryModel.builder().userIdEQ(principal.getUserId()).build());
		List<ActivityListVo> vos = list.stream().map(v -> {
			Activity activity = cacheComponent.getActivity(v.getActivityId());
			return activityComponent.buildListVo(activity);
		}).collect(Collectors.toList());
		
		model.addAttribute("historyActivities", vos.stream().filter(v -> "活动已结束".equals(v.getStatus())).collect(Collectors.toList()));
		model.addAttribute("activities", vos.stream().filter(v -> !"活动已结束".equals(v.getStatus())).collect(Collectors.toList()));
		
		return "activity/applyActivityList";
	}

	/**
	 * 团队活动报名列表
	 * @param principal
	 * @param model
     * @return
     */
	@RequestMapping("/teamApplyList")
	public String teamApplyList(Principal principal, Model model) {
        ActivityTeamApplyQueryModel activityTeamApplyQueryModel = new ActivityTeamApplyQueryModel();
        activityTeamApplyQueryModel.setPageNumber(0);
        activityTeamApplyQueryModel.setPageSize(100);
        activityTeamApplyQueryModel.setBuyerId(principal.getUserId());
        Page<ActivityTeamApply> page = activityTeamApplyService.findPage(activityTeamApplyQueryModel);
        Page<ActivityTeamApplyListVo> activityTeamApplys = PageBuilder.copyAndConvert(page, v -> activityTeamApplyComponent.buildListVo(v));
		model.addAttribute("activityTeamApplys", activityTeamApplys.getData());
		return "ucenter/activity/activityTeamApply";
	}

	@RequestMapping(value = "/collect")
	@ResponseBody
	public Result<? > collect(Long id, Principal principal, Model model) {
		activityService.collect(id, principal.getUserId());
		return ResultBuilder.ok("ok");
	}
	
	@RequestMapping("/uncollect")
	@ResponseBody
	public Result<?> uncollect(Long id, Principal principal, Model model) {
		activityService.uncollect(id, principal.getUserId());
		return ResultBuilder.ok("ok");
	}
	
	@RequestMapping("/collectList")
	public String collectList(Principal principal, Model model) {
		
		List<ActivityCollect> list = activityCollectService.findAll(ActivityCollectQueryModel.builder().userIdEQ(principal.getUserId()).build());
		List<ActivityListVo> vos = list.stream().map(v -> {
			Activity activity = cacheComponent.getActivity(v.getActivityId());
			return activityComponent.buildListVo(activity);
		}).collect(Collectors.toList());
		
		model.addAttribute("historyActivities", vos.stream().filter(v -> "活动已结束".equals(v.getStatus())).collect(Collectors.toList()));
		model.addAttribute("activities", vos.stream().filter(v -> !"活动已结束".equals(v.getStatus())).collect(Collectors.toList()));
		
		return "activity/collectActivityList";
	}

	@RequestMapping(value = "/signIn")
	public String signIn(Long id, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		ActivityApply activityApply = activityApplyService.findByActivityIdAndUserId(id, principal.getUserId());
		model.addAttribute("user", userComponent.buildSimpleVo(userService.findOne(principal.getUserId())));
		if (activityApply == null) {
			model.addAttribute("id", id);
			return "activity/signInFail";
		}
		if (activityApply.getActivityApplyStatus() == ActivityApply.ActivityApplyStatus.已报名) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("您还没有付费, 不能签到"));
			return "redirect:/activity/";
		}
		ActivitySignIn activitySignIn = activitySignInService.findByActivityIdAndUserId(id, principal.getUserId());
		if (activitySignIn != null) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("您已签到，请勿重复操作"));
			return "redirect:/activity/";
		} else {
			activityService.signIn(id, principal.getUserId());
			return "activity/signInSuccess";
		}
	}



	@RequestMapping(value = "actQrCodeApply" , method = RequestMethod.GET )
	public String actQrCodeApply(@RequestParam Long activityId , @RequestParam Long ticketId, Principal principal, RedirectAttributes redirectAttributes,Model model){
		ActivityTicket activityTicket = activityTicketService.findOne(ticketId);
		Activity activity = activityService.findOne(activityId);
		Long userId = principal.getUserId();
		User user = userService.findOne(userId);
		Date now = new Date();
		if (activity.getApplyDeadline().before(now)) {
			//活动报名已结束
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("报名失败，活动报名已结束"));
			model.addAttribute("activityId", activityId);
			return "activity/applyFail";
		} else {
			//活动报名中
			if (activityTicket != null){
				//票存在
				Long buyerId = activityTeamApplyService.findOne(activityTicket.getTeamApplyId()).getBuyerId();
				if( activityTicket.getIsUsed() == 0){
					//票未被使用过
					if ( userId.longValue() == buyerId.longValue()){
						//自己团购票，自己再使用
						ActivityApply activityApply = activityApplyService.findByActivityIdAndUserId(activityId, userId);
						if(activityApply != null){
							//活动个人报名已经操作过
							if(activityApply.getActivityApplyStatus() == ActivityApply.ActivityApplyStatus.已报名){
								//个人报名已经操作过，但是未支付，修改活动报名已支付
								activityApply.setActivityApplyStatus(ActivityApply.ActivityApplyStatus.已支付);
								activityTicket.setIsUsed(1);

								//int a = activityApplyService.update(activityApply);

								//int b = activityTicketService.update(activityTicket);

								if (activityApplyService.update(activityApply) == 0 || activityTicketService.update(activityTicket) == 0) {
									throw new ConcurrentException();
								}
								redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("活动报名成功"));
								model.addAttribute("userName", user.getNickname());
								model.addAttribute("activityId", activityId);
								return "activity/applySuccess" ;
							}else{
								//个人报名已经操作过，且已支付
								redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("您已报名过该活动，请勿重复报名"));
								return "redirect:/activity/" + activityId;
							}
						}else{
							//个人报名首次操作
							activityApplyService.useTicket(activityId,userId,buyerId,true);
							activityTicket.setIsUsed(1);
							if (activityApplyService.update(activityApply) == 0 ) {
								throw new ConcurrentException();
							}
							redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("活动报名成功"));
							model.addAttribute("userName", user.getNickname());
							model.addAttribute("activityId", activityId);
							return "activity/applySuccess" ;
						}
					}else{
						//使用他人购买的票
						ActivityApply activityApply = activityApplyService.findByActivityIdAndUserId(activityId, userId);
						if(activityApply != null){
							//活动个人报名已经操作过
							if(activityApply.getActivityApplyStatus() == ActivityApply.ActivityApplyStatus.已报名){
								//个人报名已经操作过，但是未支付，修改活动报名已支付，邀请人为购票人
								activityApply.setActivityApplyStatus(ActivityApply.ActivityApplyStatus.已支付);
								activityApply.setInviterId(buyerId);
								activityTicket.setIsUsed(1);
								if (activityApplyService.update(activityApply) == 0 || activityTicketService.update(activityTicket) == 0) {
									throw new ConcurrentException();
								}
								redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("活动报名成功"));
								model.addAttribute("userName", user.getNickname());
								model.addAttribute("activityId", activityId);
								return "activity/applySuccess" ;
							}else{
								//个人报名已经操作过，且已支付
								redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("您已报名过该活动，请勿重复报名"));
								return "redirect:/activity/" + activityId;
							}
						}else{
							//个人报名首次操作
							activityApplyService.useTicket(activityId,userId,buyerId,false);
							activityTicket.setIsUsed(1);
							if (activityApplyService.update(activityApply) == 0 ) {
								throw new ConcurrentException();
							}
							redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("活动报名成功"));
							model.addAttribute("userName", user.getNickname());
							model.addAttribute("activityId", activityId);
							return "activity/applySuccess" ;
						}
					}
				}else{
					//票已经被使用
					redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("报名失败，票已被使用过，请勿重复使用"));
					model.addAttribute("activityId", activityId);
					return "activity/applyFail";
				}
			}else {
				//票不存在
				redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("报名失败，无对应的票存在"));
				model.addAttribute("activityId", activityId);
				return "activity/applyFail";
			}
		}
	}

}
