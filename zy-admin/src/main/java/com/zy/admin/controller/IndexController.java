package com.zy.admin.controller;

import com.zy.admin.model.AdminPrincipal;
import com.zy.common.support.cache.CacheSupport;
import com.gc.component.CacheComponent;
import com.gc.component.UserComponent;
import com.gc.entity.cms.Feedback.FeedbackStatus;
import com.gc.entity.fnc.Profit;
import com.gc.entity.fnc.Withdraw.WithdrawStatus;
import com.gc.entity.sys.ConfirmStatus;
import com.gc.entity.usr.User;
import com.gc.entity.usr.User.UserType;
import com.gc.model.query.*;
import com.gc.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

import static com.zy.model.Constants.CACHE_NAME_STATISTICS;
import static com.zy.model.Constants.ProfitBizName.任务奖励;
import static com.zy.model.Constants.ProfitBizName.团队收益;

@Controller
@RequestMapping
public class IndexController {

	@Autowired
	private UserService userService;

	@Autowired
	private BankCardService userBankInfoService;

	@Autowired
	private WithdrawService withdrawService;

	@Autowired
	private FeedbackService feedbackService;

	@Autowired
	private ProfitService profitService;

	@Autowired
	private CacheSupport cacheSupport;

	@Autowired
	private CacheComponent cacheComponent;

	@Autowired
	private UserComponent userComponent;

	private static final int DEFAULT_EXPIRE = 300;

	private static final String BUYER_REGISTER_COUNT = "buyerRegisterCount";
	private static final String MERCHANT_REGISTER_COUNT = "merchantRegisterCount";
	private static final String TOTAL_REGISTER_COUNT = "totalRegisterCount";
	private static final String USER_BANK_INFO_COUNT = "userBankInfoCount";
	private static final String WITHDRAW_COUNT = "withdrawCount";
	private static final String FEEDBACK_COUNT = "feedbackCount";
	private static final String REGISTER_CHART = "registerChart";
	private static final String PROFIT_CHART = "profitChart";

	@RequestMapping("/index")
	public String index(Model model, AdminPrincipal principal) {
		Long userId = principal.getUserId();
		User user = userService.findOne(userId);
		model.addAttribute("user", userComponent.buildAdminVo(user));
		return "index";
	}

	@RequestMapping("/main")
	public String main(Model model) {

		/* 统计注册信息 */
		{
			Integer buyerCount = (Integer) cacheSupport.get(CACHE_NAME_STATISTICS, BUYER_REGISTER_COUNT);
			Integer merchantCount = (Integer) cacheSupport.get(CACHE_NAME_STATISTICS, MERCHANT_REGISTER_COUNT);
			Integer totalCount = (Integer) cacheSupport.get(CACHE_NAME_STATISTICS, TOTAL_REGISTER_COUNT);
			if (buyerCount == null || merchantCount == null || totalCount == null) {

				buyerCount = 0;
				merchantCount = 0;
				totalCount = 0;

				UserQueryModel userQueryModel = new UserQueryModel();
				List<User> users = userService.findAll(userQueryModel);
				totalCount = users.size();

				List<Long> buyerUserIds = new ArrayList<>();
				List<Long> merchantUserIds = new ArrayList<>();
				for (User user : users) {
					UserType userType = user.getUserType();
					if (userType == UserType.代理) {
						buyerCount++;
						buyerUserIds.add(user.getId());
					} else if (userType == UserType.商家) {
						merchantCount++;
						merchantUserIds.add(user.getId());
					}
				}
				cacheSupport.set(CACHE_NAME_STATISTICS, BUYER_REGISTER_COUNT, buyerCount, DEFAULT_EXPIRE);
				cacheSupport.set(CACHE_NAME_STATISTICS, MERCHANT_REGISTER_COUNT, merchantCount, DEFAULT_EXPIRE);
				cacheSupport.set(CACHE_NAME_STATISTICS, TOTAL_REGISTER_COUNT, totalCount, DEFAULT_EXPIRE);
			}

			model.addAttribute("buyerCount", buyerCount);
			model.addAttribute("merchantCount", merchantCount);
			model.addAttribute("totalCount", totalCount);
		}
		{
			Long userBankInfoUnconfirmCount = (Long) cacheSupport.get(CACHE_NAME_STATISTICS, USER_BANK_INFO_COUNT);
			if (userBankInfoUnconfirmCount == null) {
				BankCardQueryModel userBankInfoQueryModel = new BankCardQueryModel();
				userBankInfoQueryModel.setConfirmStatusEQ(ConfirmStatus.未审核);
				userBankInfoUnconfirmCount = userBankInfoService.count(userBankInfoQueryModel);

				cacheSupport.set(CACHE_NAME_STATISTICS, USER_BANK_INFO_COUNT, userBankInfoUnconfirmCount, DEFAULT_EXPIRE);
			}
			model.addAttribute("userBankInfoCount", userBankInfoUnconfirmCount);
		}
		{
			Long withdrawCount = (Long) cacheSupport.get(CACHE_NAME_STATISTICS, WITHDRAW_COUNT);
			if (withdrawCount == null) {
				WithdrawQueryModel withdrawQueryModel = new WithdrawQueryModel();
				withdrawQueryModel.setWithdrawStatusEQ(WithdrawStatus.已申请);
				withdrawCount = withdrawService.count(withdrawQueryModel);

				cacheSupport.set(CACHE_NAME_STATISTICS, WITHDRAW_COUNT, withdrawCount, DEFAULT_EXPIRE);
			}

			model.addAttribute("withdrawCount", withdrawCount);
		}
		/* 统计财务信息 */

		/* 统计待处理信息 */
		{
			Long feedbackCount = (Long) cacheSupport.get(CACHE_NAME_STATISTICS, FEEDBACK_COUNT);
			if (feedbackCount == null) {
				FeedbackQueryModel feedbackQueryModel = new FeedbackQueryModel();
				feedbackQueryModel.setFeedbackStatusEQ(FeedbackStatus.等待客服接手);
				feedbackCount = feedbackService.count(feedbackQueryModel);

				cacheSupport.set(CACHE_NAME_STATISTICS, FEEDBACK_COUNT, feedbackCount, DEFAULT_EXPIRE);
			}

			model.addAttribute("feedbackCount", feedbackCount);
		}
		return "main";
	}

	@RequestMapping("/main/ajaxChart/register")
	@ResponseBody
	public Map<String, Object> getRegisterChart() {

		@SuppressWarnings("unchecked")
		Map<String, Object> dataMap = (Map<String, Object>) cacheSupport.get(CACHE_NAME_STATISTICS, REGISTER_CHART);
		if (dataMap == null) {
			Date now = new Date();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.add(Calendar.DATE, -15);

			UserQueryModel userQueryModel = new UserQueryModel();
			userQueryModel.setPageSize(null);
			userQueryModel.setRegisterTimeGTE(calendar.getTime());
			List<User> users = userService.findAll(userQueryModel);

			final String dateFmt = "yyyy-MM-dd";
			Date date = null;
			List<Long> userCountList = new ArrayList<Long>();
			List<Long> buyerCountList = new ArrayList<Long>();
			List<Long> merchantCountList = new ArrayList<Long>();
			List<String> dataStrs = new ArrayList<String>();
			while ((date = calendar.getTime()).before(now)) {
				long userCount = 0L;
				long buyerCount = 0L;
				long merchantCount = 0L;
				String dateStr = DateFormatUtils.format(date, dateFmt);

				for (User user : users) {
					if (DateFormatUtils.format(user.getRegisterTime(), dateFmt).equals(dateStr)) {
						UserType userType = user.getUserType();
						if (userType == UserType.代理) {
							buyerCount++;
						}
						if (userType == UserType.商家) {
							merchantCount++;
						}
						userCount++;
					}
				}

				userCountList.add(userCount);
				buyerCountList.add(buyerCount);
				merchantCountList.add(merchantCount);
				dataStrs.add(StringUtils.right(dateStr, 5));

				calendar.add(Calendar.DATE, 1);
			}
			dataMap = new LinkedHashMap<String, Object>();
			dataMap.put("userCount", userCountList);
			dataMap.put("buyerCount", buyerCountList);
			dataMap.put("merchantCount", merchantCountList);
			dataMap.put("chartLabel", dataStrs);

			cacheSupport.set(CACHE_NAME_STATISTICS, REGISTER_CHART, dataMap, DEFAULT_EXPIRE);
		}

		return dataMap;
	}

	@RequestMapping("/main/ajaxChart/profit")
	@ResponseBody
	public Map<String, Object> getpRrofitChart() {
		@SuppressWarnings("unchecked")
		Map<String, Object> dataMap = (Map<String, Object>) cacheSupport.get(CACHE_NAME_STATISTICS, PROFIT_CHART);
		if (dataMap == null) {
			Date now = new Date();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.add(Calendar.DATE, -15);

			ProfitQueryModel profitQueryModel = new ProfitQueryModel();
			profitQueryModel.setCreatedTimeGTE(calendar.getTime());
			List<Profit> profits = profitService.findAll(profitQueryModel);

			final String dateFmt = "yyyy-MM-dd";
			Date date = null;
			List<BigDecimal> feeAmountList = new ArrayList<BigDecimal>();
			List<BigDecimal> accountAmountList = new ArrayList<BigDecimal>();
			List<BigDecimal> teamAmountList = new ArrayList<BigDecimal>();
			List<String> dataStrs = new ArrayList<String>();
			while ((date = calendar.getTime()).before(now)) {
				BigDecimal feeAmount = new BigDecimal("0.00");
				BigDecimal accountAmount = new BigDecimal("0.00");
				BigDecimal teamAmount = new BigDecimal("0.00");

				String dateStr = DateFormatUtils.format(date, dateFmt);

				for (Profit profit : profits) {
					if (DateFormatUtils.format(profit.getCreatedTime(), dateFmt).equals(dateStr)) {
						switch (profit.getBizName()) {
						case 任务奖励:
							User user = cacheComponent.getUser(profit.getUserId());
							if (user.getUserType() == UserType.平台) {
								feeAmount = feeAmount.add(profit.getAmount());
							} else {
								accountAmount = accountAmount.add(profit.getAmount());
							}
							break;
						case 团队收益:
							teamAmount = teamAmount.add(profit.getAmount());
							break;
						default:
							break;
						}
					}
				}

				feeAmountList.add(feeAmount);
				accountAmountList.add(accountAmount);
				teamAmountList.add(teamAmount);
				dataStrs.add(StringUtils.right(dateStr, 5));
				calendar.add(Calendar.DATE, 1);
			}
			dataMap = new LinkedHashMap<String, Object>();
			dataMap.put("feeAmount", feeAmountList);
			dataMap.put("accountAmount", accountAmountList);
			dataMap.put("teamAmount", teamAmountList);
			dataMap.put("chartLabel", dataStrs);

			cacheSupport.set(CACHE_NAME_STATISTICS, PROFIT_CHART, dataMap, DEFAULT_EXPIRE);
		}
		return dataMap;
	}

	@RequestMapping("/dev")
	public String dev() {
		return "dev";
	}

	@RequestMapping({ "", "/login" })
	public String login(HttpServletRequest request, HttpServletResponse response) {
		Subject subject = SecurityUtils.getSubject();
		if (subject.getPrincipal() != null) {
			return "redirect:/index";
		}
		String authenticationException = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		if (authenticationException != null) {
			switch (authenticationException) {
			case "com.sk.admin.extend.IncorrectCaptchaException":
				request.setAttribute("failureInfo", "登录失败, 验证码错误");
				break;
			default:
				request.setAttribute("failureInfo", "登录失败, 用户名或者密码错误");
				break;
			}
		}
		return "login";
	}

}
