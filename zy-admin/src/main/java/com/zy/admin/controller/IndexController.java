package com.zy.admin.controller;

import com.alibaba.dubbo.remoting.TimeoutException;
import com.zy.Config;
import com.zy.admin.model.AdminPrincipal;
import com.zy.common.support.cache.CacheSupport;
import com.zy.component.LocalCacheComponent;
import com.zy.component.UserComponent;
import com.zy.entity.fnc.Deposit.DepositStatus;
import com.zy.entity.fnc.PayType;
import com.zy.entity.fnc.Payment.PaymentStatus;
import com.zy.entity.fnc.Withdraw.WithdrawStatus;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserType;
import com.zy.model.Constants;
import com.zy.model.query.*;
import com.zy.service.*;
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
import java.util.*;
import java.util.stream.Collectors;

import static com.zy.model.Constants.*;

@Controller
@RequestMapping
public class IndexController {

	@Autowired
	private UserService userService;

	@Autowired
	private BankCardService userBankInfoService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private DepositService depositService;

	@Autowired
	private WithdrawService withdrawService;

	@Autowired
	private ReportService reportService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserInfoService userInfoService;


	@Autowired
	private Config config;

	@Autowired
	private UserComponent userComponent;

	@Autowired
	private LocalCacheComponent localCacheComponent;

	@Autowired
	private CacheSupport cacheSupport;

	private static final int DEFAULT_EXPIRE = 300;

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
			Long agentCount = (Long) cacheSupport.get(CACHE_NAME_STATISTICS, Constants.CACHE_NAME_AGENT_REGISTER_COUNT);
			if (agentCount == null) {
				agentCount = userService.count(UserQueryModel.builder().userTypeEQ(UserType.代理).build());
				cacheSupport.set(CACHE_NAME_STATISTICS, Constants.CACHE_NAME_AGENT_REGISTER_COUNT, agentCount, DEFAULT_EXPIRE);
			}
			model.addAttribute("agentCount", agentCount);

		}
			
		/* 统计待处理信息 */
		{
			Long userBankInfoUnconfirmCount = (Long) cacheSupport.get(CACHE_NAME_STATISTICS, CACHE_NAME_USER_BANK_INFO_COUNT);
			if (userBankInfoUnconfirmCount == null) {
				BankCardQueryModel userBankInfoQueryModel = new BankCardQueryModel();
				userBankInfoQueryModel.setConfirmStatusEQ(ConfirmStatus.待审核);
				userBankInfoUnconfirmCount = userBankInfoService.count(userBankInfoQueryModel);

				cacheSupport.set(CACHE_NAME_STATISTICS, CACHE_NAME_USER_BANK_INFO_COUNT, userBankInfoUnconfirmCount, DEFAULT_EXPIRE);
			}
			model.addAttribute("userBankInfoCount", userBankInfoUnconfirmCount);

			Long userInfoCount = (Long) cacheSupport.get(CACHE_NAME_STATISTICS, CACHE_NAME_USER_INFO_COUNT);
			if (userInfoCount == null) {
				userInfoCount = userInfoService.count(UserInfoQueryModel.builder().confirmStatusEQ(ConfirmStatus.待审核).build());

				cacheSupport.set(CACHE_NAME_STATISTICS, CACHE_NAME_USER_INFO_COUNT, userInfoCount, DEFAULT_EXPIRE);
			}
			model.addAttribute("userInfoCount", userInfoCount);

			Long reportPreCount = (Long) cacheSupport.get(CACHE_NAME_STATISTICS, CACHE_NAME_REPORT_PRE_COUNT);
			if (reportPreCount == null) {
				reportPreCount = reportService.count(ReportQueryModel.builder().preConfirmStatusEQ(ConfirmStatus.待审核).build());

				cacheSupport.set(CACHE_NAME_STATISTICS, CACHE_NAME_REPORT_PRE_COUNT, reportPreCount, DEFAULT_EXPIRE);
			}
			model.addAttribute("reportPreCount", reportPreCount);

			Long reportCount = (Long) cacheSupport.get(CACHE_NAME_STATISTICS, CACHE_NAME_REPORT_COUNT);
			if (reportCount == null) {
				reportCount = reportService.count(ReportQueryModel.builder().confirmStatusEQ(ConfirmStatus.待审核).build());

				cacheSupport.set(CACHE_NAME_STATISTICS, CACHE_NAME_REPORT_COUNT, reportCount, DEFAULT_EXPIRE);
			}
			model.addAttribute("reportCount", reportCount);

			Long orderPlatformDeliverCount = (Long) cacheSupport.get(CACHE_NAME_STATISTICS, CACHE_NAME_ORDER_PLATFORM_DELIVER_COUNT);
			if (orderPlatformDeliverCount == null) {
				Long sysUserId = config.getSysUserId();
				orderPlatformDeliverCount = orderService.count(OrderQueryModel.builder().sellerIdEQ(sysUserId).orderStatusEQ(OrderStatus.已支付).build());

				cacheSupport.set(CACHE_NAME_STATISTICS, CACHE_NAME_ORDER_PLATFORM_DELIVER_COUNT, orderPlatformDeliverCount, DEFAULT_EXPIRE);
			}
			model.addAttribute("orderPlatformDeliverCount", orderPlatformDeliverCount);
			
			/* 财务相关 */
			Long withdrawCount = (Long) cacheSupport.get(CACHE_NAME_STATISTICS, CACHE_NAME_WITHDRAW_COUNT);
			if (withdrawCount == null) {
				WithdrawQueryModel withdrawQueryModel = new WithdrawQueryModel();
				withdrawQueryModel.setWithdrawStatusEQ(WithdrawStatus.已申请);
				withdrawCount = withdrawService.count(withdrawQueryModel);

				cacheSupport.set(CACHE_NAME_STATISTICS, CACHE_NAME_WITHDRAW_COUNT, withdrawCount, DEFAULT_EXPIRE);
			}
			model.addAttribute("withdrawCount", withdrawCount);

			Long paymentCount = (Long) cacheSupport.get(CACHE_NAME_STATISTICS, CACHE_NAME_PAYMENT_COUNT);
			if (paymentCount == null) {
				paymentCount = paymentService.count(PaymentQueryModel.builder().paymentStatusEQ(PaymentStatus.待确认).payTypeEQ(PayType.银行汇款).build());

				cacheSupport.set(CACHE_NAME_STATISTICS, CACHE_NAME_PAYMENT_COUNT, paymentCount, DEFAULT_EXPIRE);
			}
			model.addAttribute("paymentCount", paymentCount);

			Long depositCount = (Long) cacheSupport.get(CACHE_NAME_STATISTICS, CACHE_NAME_DEPOSIT_COUNT);
			if (depositCount == null) {
				depositCount = depositService.count(DepositQueryModel.builder().depositStatusEQ(DepositStatus.待确认).payTypeEQ(PayType.银行汇款).build());

				cacheSupport.set(CACHE_NAME_STATISTICS, CACHE_NAME_DEPOSIT_COUNT, depositCount, DEFAULT_EXPIRE);
			}
			model.addAttribute("depositCount", depositCount);
		}
		return "main";
	}

	@RequestMapping("/main/ajaxChart/register")
	@ResponseBody
	public Map<String, Object> getRegisterChart() {

		@SuppressWarnings("unchecked")
		Map<String, Object> dataMap = (Map<String, Object>) cacheSupport.get(CACHE_NAME_STATISTICS, Constants.CACHE_NAME_REGISTER_CHART);
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
			userQueryModel.setUserTypeEQ(UserType.代理);
			userQueryModel.setRegisterTimeGTE(calendar.getTime());
			List<User> users = userService.findAll(userQueryModel);

			final String dateFmt = "yyyy-MM-dd";
			Date date = null;
			List<Long> userCountList = new ArrayList<Long>();
			List<String> dataStrs = new ArrayList<String>();
			while ((date = calendar.getTime()).before(now)) {
				long agentCount = 0L;
				String dateStr = DateFormatUtils.format(date, dateFmt);

				for (User user : users) {
					if (DateFormatUtils.format(user.getRegisterTime(), dateFmt).equals(dateStr)) {
						agentCount++;
					}
				}

				userCountList.add(agentCount);
				dataStrs.add(StringUtils.right(dateStr, 5));

				calendar.add(Calendar.DATE, 1);
			}
			dataMap = new LinkedHashMap<String, Object>();
			dataMap.put("userCount", userCountList);
			dataMap.put("chartLabel", dataStrs);

			cacheSupport.set(CACHE_NAME_STATISTICS, Constants.CACHE_NAME_REGISTER_CHART, dataMap, DEFAULT_EXPIRE);
		}

		return dataMap;
	}

	@RequestMapping("/main/ajaxChart/order")
	@ResponseBody
	public Map<String, Object> getOrderChart() {

		@SuppressWarnings("unchecked")
		Map<String, Object> dataMap = (Map<String, Object>) cacheSupport.get(CACHE_NAME_STATISTICS, Constants.CACHE_NAME_ORDER_CHART);
		if (dataMap == null) {
			Date now = new Date();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.add(Calendar.DATE, -15);

			//List<Order> orders = orderService.findAll(OrderQueryModel.builder().orderStatusIN(new OrderStatus[] {OrderStatus.已支付, OrderStatus.已发货, OrderStatus.已完成}).paidTimeGTE(calendar.getTime()).build());
			List<Order> orders = localCacheComponent.getOrders().stream()
					.filter(v -> {
						OrderStatus orderStatus = v.getOrderStatus();
						if (orderStatus == OrderStatus.已支付 || orderStatus == OrderStatus.已发货 || orderStatus == OrderStatus.已完成) {
							return true;
						}
						return false;
					})
					.filter(v -> {
						Date paidTime = v.getPaidTime();
						if (paidTime.after(calendar.getTime()) || calendar.getTime().equals(paidTime)) {
							return true;
						}
						return false;
					}).collect(Collectors.toList());

			final String dateFmt = "yyyy-MM-dd";
			Date date = null;
			List<Long> orderList = new ArrayList<Long>();
			List<String> dataStrs = new ArrayList<String>();
			while ((date = calendar.getTime()).before(now)) {
				String dateStr = DateFormatUtils.format(date, dateFmt);
				Long count = 0L;
				for (Order order : orders) {
					Date paidTime = order.getPaidTime();
					if (paidTime != null && DateFormatUtils.format(paidTime, dateFmt).equals(dateStr)) {
						count = count + order.getQuantity();
					}
				}

				orderList.add(count);
				dataStrs.add(StringUtils.right(dateStr, 5));

				calendar.add(Calendar.DATE, 1);
			}
			dataMap = new LinkedHashMap<String, Object>();
			dataMap.put("orderCount", orderList);
			dataMap.put("chartLabel", dataStrs);

			cacheSupport.set(CACHE_NAME_STATISTICS, Constants.CACHE_NAME_ORDER_CHART, dataMap, DEFAULT_EXPIRE);
		}

		return dataMap;
	}

	@RequestMapping("/dev")
	public String dev() {
		return "dev";
	}

	@RequestMapping({"", "/login"})
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

	@RequestMapping("/settlementMonthly")
	@ResponseBody
	public String orderSettlementMonthlyJob(String yeahAndMonth, HttpServletRequest request) {
		try {
			if (StringUtils.isNotBlank(yeahAndMonth)) {
				Date beginDate = cacheSupport.get("settlementMonthlyKey", yeahAndMonth);
				if (beginDate == null) {
					cacheSupport.set("settlementMonthlyKey", yeahAndMonth, new Date(), 60);
					orderService.settleUpMonthly(yeahAndMonth);
					cacheSupport.delete("settlementMonthlyKey", yeahAndMonth);
					return yeahAndMonth + "的结算处理完成";
				} else {
					return yeahAndMonth + "的结算正在运行中 结算开始时间为：" + beginDate;
				}
			} else {
				return "传入的日期不合法" + yeahAndMonth;
			}
		} catch (Exception e) {
			cacheSupport.delete("settlementMonthlyKey", yeahAndMonth);
			return e.getMessage();
		}

	}

	@RequestMapping("/settleUpProfit")
	@ResponseBody
	public String settleUpProfit(String yeahAndMonth, HttpServletRequest request) {
		try {
			if (StringUtils.isNotBlank(yeahAndMonth)) {
				Date beginDate = cacheSupport.get("settleUpProfitKey", yeahAndMonth);
				if (beginDate == null) {
					cacheSupport.set("settleUpProfitKey", yeahAndMonth, new Date(), 60);
					orderService.settleUpProfit(yeahAndMonth);
					cacheSupport.delete("settleUpProfitKey", yeahAndMonth);
					return yeahAndMonth + "的结算处理完成";
				} else {
					return yeahAndMonth + "的结算正在运行中 结算开始时间为：" + beginDate;
				}
			} else {
				return "传入的日期不合法" + yeahAndMonth;
			}
		} catch (Exception e) {
			cacheSupport.delete("settleUpProfitKey", yeahAndMonth);
			return e.getMessage();
		}

	}

	@RequestMapping("/settleUpOption")
	@ResponseBody
	public String settleUpOption(String yeahAndMonth, HttpServletRequest request) {
		try {
			if (StringUtils.isNotBlank(yeahAndMonth)) {
				Date beginDate = cacheSupport.get("settleUpOption", yeahAndMonth);
				if (beginDate == null) {
					cacheSupport.set("settleUpOption", yeahAndMonth, new Date(), 60);
					orderService.settleUpOption(yeahAndMonth);
					cacheSupport.delete("settleUpOption", yeahAndMonth);
					return yeahAndMonth + "的期权结算处理完成";
				} else {
					return yeahAndMonth + "的结算正在运行中 结算开始时间为：" + beginDate;
				}
			} else {
				return "传入的日期不合法" + yeahAndMonth;
			}
		} catch (Exception e) {
			cacheSupport.delete("settleUpOption", yeahAndMonth);
			return e.getMessage();
		}

	}

	@RequestMapping("/settleUpRebate")
	@ResponseBody
	public String settleUpRebate(String yeahAndMonth, HttpServletRequest request) {
		try {
			if (StringUtils.isNotBlank(yeahAndMonth)) {
				Date beginDate = cacheSupport.get("settleUpOption", yeahAndMonth);
				if (beginDate == null) {
					cacheSupport.set("settleUpOption", yeahAndMonth, new Date(), 60);
					orderService.settleUpRebate(yeahAndMonth);
					cacheSupport.delete("settleUpOption", yeahAndMonth);
					return yeahAndMonth + "的返利结算处理完成";
				} else {
					return yeahAndMonth + "的返利结算正在运行中 结算开始时间为：" + beginDate;
				}
			} else {
				return "传入的日期不合法" + yeahAndMonth;
			}
		} catch (Exception e) {
			cacheSupport.delete("settleUpOption", yeahAndMonth);
			return e.getMessage();
		}

	}

	@RequestMapping("/settleUpDirector")
	@ResponseBody
	public String settleUpDirector(String yeahAndMonth, HttpServletRequest request) {
		try {
			if (StringUtils.isNotBlank(yeahAndMonth)) {
				Date beginDate = cacheSupport.get("settleUpOption", yeahAndMonth);
				if (beginDate == null) {
					cacheSupport.set("settleUpOption", yeahAndMonth, new Date(), 60);
					orderService.settleUpDirector(yeahAndMonth);
					cacheSupport.delete("settleUpOption", yeahAndMonth);
					return yeahAndMonth + "的董事贡献奖励结算处理完成";
				} else {
					return yeahAndMonth + "的董事贡献奖励结算正在运行中 结算开始时间为：" + beginDate;
				}
			} else {
				return "传入的日期不合法" + yeahAndMonth;
			}
		} catch (Exception e) {
			cacheSupport.delete("settleUpOption", yeahAndMonth);
			return e.getMessage();
		}

	}
}
