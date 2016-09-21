package com.zy.admin.controller;

import static com.zy.model.Constants.CACHE_NAME_STATISTICS;
import static com.zy.model.Constants.CACHE_NAME_APPEARANCE_COUNT;
import static com.zy.model.Constants.CACHE_NAME_REPORT_PRE_COUNT;
import static com.zy.model.Constants.CACHE_NAME_REPORT_COUNT;
import static com.zy.model.Constants.CACHE_NAME_USER_BANK_INFO_COUNT;
import static com.zy.model.Constants.CACHE_NAME_WITHDRAW_COUNT;
import static com.zy.model.Constants.CACHE_NAME_PROFIT_CHART;
import static com.zy.model.Constants.CACHE_NAME_ORDER_PLATFORM_DELIVER_COUNT;
import static com.zy.model.Constants.CACHE_NAME_PAYMENT_COUNT;
import static com.zy.model.Constants.CACHE_NAME_DEPOSIT_COUNT;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.zy.admin.model.AdminPrincipal;
import com.zy.common.support.cache.CacheSupport;
import com.zy.component.CacheComponent;
import com.zy.component.UserComponent;
import com.zy.entity.fnc.Deposit.DepositStatus;
import com.zy.entity.fnc.PayType;
import com.zy.entity.fnc.Payment.PaymentStatus;
import com.zy.entity.fnc.Profit;
import com.zy.entity.fnc.Withdraw.WithdrawStatus;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserType;
import com.zy.model.Constants;
import com.zy.model.query.AppearanceQueryModel;
import com.zy.model.query.BankCardQueryModel;
import com.zy.model.query.DepositQueryModel;
import com.zy.model.query.OrderQueryModel;
import com.zy.model.query.PaymentQueryModel;
import com.zy.model.query.ProfitQueryModel;
import com.zy.model.query.ReportQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.model.query.WithdrawQueryModel;
import com.zy.service.AppearanceService;
import com.zy.service.BankCardService;
import com.zy.service.DepositService;
import com.zy.service.OrderService;
import com.zy.service.PaymentService;
import com.zy.service.ProfitService;
import com.zy.service.ReportService;
import com.zy.service.UserService;
import com.zy.service.WithdrawService;

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
	private ProfitService profitService;
	
	@Autowired
	private WithdrawService withdrawService;

	@Autowired
	private AppearanceService appearanceService;

	@Autowired
	private ReportService reportService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CacheSupport cacheSupport;

	@Autowired
	private CacheComponent cacheComponent;

	@Autowired
	private UserComponent userComponent;

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
	
			Long appearanceCount = (Long) cacheSupport.get(CACHE_NAME_STATISTICS, CACHE_NAME_APPEARANCE_COUNT);
			if(appearanceCount == null) {
				appearanceCount = appearanceService.count(AppearanceQueryModel.builder().confirmStatusEQ(ConfirmStatus.未通过).build());
				
				cacheSupport.set(CACHE_NAME_STATISTICS, CACHE_NAME_APPEARANCE_COUNT, appearanceCount, DEFAULT_EXPIRE);
			}
			model.addAttribute("appearanceCount", appearanceCount);

			Long reportPreCount = (Long) cacheSupport.get(CACHE_NAME_STATISTICS, CACHE_NAME_REPORT_PRE_COUNT);
			if(reportPreCount == null) {
				reportPreCount = reportService.count(ReportQueryModel.builder().preConfirmStatusEQ(ConfirmStatus.未通过).build());
				
				cacheSupport.set(CACHE_NAME_STATISTICS, CACHE_NAME_REPORT_PRE_COUNT, reportPreCount, DEFAULT_EXPIRE);
			}
			model.addAttribute("reportPreCount", reportPreCount);
			
			Long reportCount = (Long) cacheSupport.get(CACHE_NAME_STATISTICS, CACHE_NAME_REPORT_COUNT);
			if(reportCount == null) {
				reportCount = reportService.count(ReportQueryModel.builder().confirmStatusEQ(ConfirmStatus.未通过).build());
				
				cacheSupport.set(CACHE_NAME_STATISTICS, CACHE_NAME_REPORT_COUNT, reportCount, DEFAULT_EXPIRE);
			}
			model.addAttribute("reportCount", reportCount);
			
			Long orderPlatformDeliverCount = (Long) cacheSupport.get(CACHE_NAME_STATISTICS, CACHE_NAME_ORDER_PLATFORM_DELIVER_COUNT);
			if(orderPlatformDeliverCount == null) {
				orderPlatformDeliverCount = orderService.count(OrderQueryModel.builder().isPlatformDeliverEQ(true).orderStatusEQ(OrderStatus.已支付).build());
				
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
			if(paymentCount == null) {
				paymentCount = paymentService.count(PaymentQueryModel.builder().paymentStatusEQ(PaymentStatus.待确认).payTypeEQ(PayType.银行汇款).build());
				
				cacheSupport.set(CACHE_NAME_STATISTICS, CACHE_NAME_PAYMENT_COUNT, paymentCount, DEFAULT_EXPIRE);
			}
			model.addAttribute("paymentCount", paymentCount);
			
			Long depositCount = (Long) cacheSupport.get(CACHE_NAME_STATISTICS, CACHE_NAME_DEPOSIT_COUNT);
			if(depositCount == null) {
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

			List<Order> orders = orderService.findAll(OrderQueryModel.builder().createdTimeGTE(calendar.getTime()).build());
			
			final String dateFmt = "yyyy-MM-dd";
			Date date = null;
			List<Long> orderList = new ArrayList<Long>();
			List<String> dataStrs = new ArrayList<String>();
			while ((date = calendar.getTime()).before(now)) {
				String dateStr = DateFormatUtils.format(date, dateFmt);
				Long count = 0L;
				for (Order order : orders) {
					if (DateFormatUtils.format(order.getCreatedTime(), dateFmt).equals(dateStr)) {
						count++;
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
