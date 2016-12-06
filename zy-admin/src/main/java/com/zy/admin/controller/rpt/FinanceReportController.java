package com.zy.admin.controller.rpt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;




import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;




import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.LocalCacheComponent;
import com.zy.component.ProfitComponent;
import com.zy.entity.fnc.Account;
import com.zy.entity.fnc.Deposit;
import com.zy.entity.fnc.Payment;
import com.zy.entity.fnc.Profit;
import com.zy.entity.fnc.Withdraw;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.model.FinanceReportVo;
import com.zy.util.GcUtils;
import com.zy.vo.ProfitAdminVo;

@Controller
@RequestMapping("/report/finance")
public class FinanceReportController {
	
	@Autowired
	private LocalCacheComponent localCacheComponent;
	
	@Autowired
	private ProfitComponent profitComponent;
	
	@RequiresPermissions("financeReport:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("userRankMap", Arrays.asList(User.UserRank.values()).stream().collect(Collectors.toMap(v->v, v-> GcUtils.getUserRankLabel(v),(u, v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); }, LinkedHashMap::new)) );
		return "rpt/financeReport";
	}
	
	@RequiresPermissions("financeReport:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<FinanceReportVo> list(FinanceReportVo.FinanceReportVoQueryModel financeReportVoQueryModel) {
		
		List<User> date = new ArrayList<User>();
		List<User> users = localCacheComponent.getUsers();
		List<User> filterUser = users.stream().filter(user -> {
			boolean result = true;
			String nicknameLK = financeReportVoQueryModel.getNicknameLK();
			String phoneEQ = financeReportVoQueryModel.getPhoneEQ();
			UserRank userRankEQ = financeReportVoQueryModel.getUserRankEQ();
			
			if (!StringUtils.isBlank(nicknameLK)) {
				result = result && StringUtils.contains(user.getNickname(), nicknameLK);
			}
			if (!StringUtils.isBlank(phoneEQ)) {
				result = result && phoneEQ.equals(user.getPhone());
			}
			if(userRankEQ != null) {
				result = result && userRankEQ == user.getUserRank();
			}
			return result;
		}).collect(Collectors.toList());
		List<Long> userIds = filterUser.stream().map(v -> v.getId()).collect(Collectors.toList());
		if(userIds.isEmpty()) {
			return new Grid<>(PageBuilder.empty(financeReportVoQueryModel.getPageSize(), 0));
		}
		Map<Long, Boolean> userIdMap = userIds.stream().collect(Collectors.toMap(v -> v, v -> true));
		
		List<Payment> payments = localCacheComponent.getPayments();
		List<Deposit> deposits = localCacheComponent.getDeposits();
		List<Withdraw> withdraws = localCacheComponent.getWithdraws();
		List<Profit> profits = localCacheComponent.getProfits();
		List<Account> accounts = localCacheComponent.getAccounts();
		
		List<Payment> filterPayments = payments.stream()
			.filter(payment -> {
				
			boolean result = userIdMap.get(payment.getUserId()) != null;
			
			Date paymentPaidTimeGTE = financeReportVoQueryModel.getTimeGTE();
			Date paymentPaidTimeLT = financeReportVoQueryModel.getTimeLT();
			
			if (paymentPaidTimeGTE != null) {
				result = result && (payment.getPaidTime().after(paymentPaidTimeGTE) || payment.equals(paymentPaidTimeGTE));
			}
			if (paymentPaidTimeLT != null) {
				result = result && payment.getPaidTime().before(paymentPaidTimeLT);
			}
			return result;
		}).collect(Collectors.toList());
		
		List<Deposit> filterDeposits = deposits.stream()
			.filter(deposit -> {
				
			boolean result = userIdMap.get(deposit.getUserId()) != null;
			
			Date depositPaidTimeGTE = financeReportVoQueryModel.getTimeGTE();
			Date depositPaidTimeLT = financeReportVoQueryModel.getTimeLT();
			
			Date paidTime = deposit.getPaidTime();
			if (depositPaidTimeGTE != null) {
				result = result && (paidTime.after(depositPaidTimeGTE) || depositPaidTimeGTE.equals(paidTime));
			}
			if (depositPaidTimeLT != null) {
				result = result && paidTime.before(depositPaidTimeLT);
			}
			return result;
		}).collect(Collectors.toList());
		
		List<Withdraw> filterWithdraws = withdraws.stream()
			.filter(withdraw -> {
				
			boolean result = userIdMap.get(withdraw.getUserId()) != null;
			
			Date withdrawWithdrawedTimeGTE = financeReportVoQueryModel.getTimeGTE();
			Date withdrawWithdrawedTimeLT = financeReportVoQueryModel.getTimeLT();
			
			Date withdrawedTime = withdraw.getWithdrawedTime();
			if (withdrawWithdrawedTimeGTE != null) {
				result = result && (withdrawedTime.after(withdrawWithdrawedTimeGTE) || withdrawWithdrawedTimeGTE.equals(withdrawedTime));
			}
			if (withdrawWithdrawedTimeLT != null) {
				result = result && withdrawedTime.before(withdrawWithdrawedTimeLT);
			}
			return result;
		}).collect(Collectors.toList());
		
		List<Profit> filterProfits = profits.stream()
			.filter(profit -> {
				
			boolean result = userIdMap.get(profit.getUserId()) != null;
			
			Date profitGrantedTimeGTE = financeReportVoQueryModel.getTimeGTE();
			Date profitGrantedTimeLT = financeReportVoQueryModel.getTimeLT();
			
			Date grantedTime = profit.getGrantedTime();
			if (profitGrantedTimeGTE != null) {
				result = result && (grantedTime.after(profitGrantedTimeGTE) || profitGrantedTimeGTE.equals(grantedTime));
			}
			if (profitGrantedTimeLT != null) {
				result = result && grantedTime.before(profitGrantedTimeLT);
			}
			return result;
		}).collect(Collectors.toList());
		
		List<Account> filterAccounts = accounts.stream()
			.filter(account -> {
			return userIdMap.get(account.getUserId()) != null;
		}).collect(Collectors.toList());
		
		int totalCount = filterUser.size();
		Integer pageSize = financeReportVoQueryModel.getPageSize();
		Integer pageNumber = financeReportVoQueryModel.getPageNumber();
		if (pageSize == null) {
			pageSize = 20;
		} else if (pageSize == -1) {
			pageSize = totalCount + 1;
		}
		if (pageNumber == null) {
			pageNumber = 0;
		}
		int from = pageNumber * pageSize;
		if (from < totalCount) {
			int to = (from + pageSize) > totalCount ? totalCount : from + pageSize;
			for (int index = from; index < to; index++) {
				date.add(filterUser.get(index));
			}
		}
		
		BigDecimal zero = new BigDecimal("0.00");
		Map<Long, FinanceReportVo> userFinanceReportMap = date.stream().collect(Collectors.toMap(v -> v.getId(), v -> {
			FinanceReportVo financeReportVo = new FinanceReportVo();
			financeReportVo.setUserId(v.getId());
			financeReportVo.setUserNickname(v.getNickname());
			financeReportVo.setUserPhone(v.getPhone());
			financeReportVo.setPaymentAmount(zero);
			financeReportVo.setDepositAmount(zero);
			financeReportVo.setWithdrawAmount(zero);
			financeReportVo.setProfitAmount(zero);
			financeReportVo.setAccountAmount(zero);
			return financeReportVo;
		}));
		
		for(Payment payment : filterPayments) {
			Long userId = payment.getUserId();
			FinanceReportVo financeReportVo = userFinanceReportMap.get(userId);
			if(financeReportVo != null) {
				BigDecimal amount = financeReportVo.getPaymentAmount();
				if(amount == null) {
					amount = new BigDecimal("0.00");
				}
				amount = amount.add(payment.getAmount1());
				financeReportVo.setPaymentAmount(amount);
				
				userFinanceReportMap.put(userId, financeReportVo);
			}
		}
		
		for(Deposit deposit : filterDeposits) {
			Long userId = deposit.getUserId();
			FinanceReportVo financeReportVo = userFinanceReportMap.get(userId);
			if(financeReportVo != null) {
				BigDecimal amount = financeReportVo.getDepositAmount();
				if(amount == null) {
					amount = new BigDecimal("0.00");
				}
				amount = amount.add(deposit.getAmount1());
				financeReportVo.setDepositAmount(amount);
				
				userFinanceReportMap.put(userId, financeReportVo);
			}
		}

		for(Withdraw withdraw : filterWithdraws) {
			Long userId = withdraw.getUserId();
			FinanceReportVo financeReportVo = userFinanceReportMap.get(userId);
			if(financeReportVo != null) {
				BigDecimal amount = financeReportVo.getWithdrawAmount();
				if(amount == null) {
					amount = new BigDecimal("0.00");
				}
				amount = amount.add(withdraw.getAmount());
				financeReportVo.setWithdrawAmount(amount);
				
				userFinanceReportMap.put(userId, financeReportVo);
			}
		}
		
		for(Profit profit : filterProfits) {
			Long userId = profit.getUserId();
			FinanceReportVo financeReportVo = userFinanceReportMap.get(userId);
			if(financeReportVo != null) {
				BigDecimal amount = financeReportVo.getProfitAmount();
				if(amount == null) {
					amount = new BigDecimal("0.00");
				}
				amount = amount.add(profit.getAmount());
				financeReportVo.setProfitAmount(amount);
				
				userFinanceReportMap.put(userId, financeReportVo);
			}
		}

		for(Account account : filterAccounts) {
			Long userId = account.getUserId();
			FinanceReportVo financeReportVo = userFinanceReportMap.get(userId);
			if(financeReportVo != null) {
				BigDecimal amount = financeReportVo.getAccountAmount();
				if(amount == null) {
					amount = new BigDecimal("0.00");
				}
				amount = amount.add(account.getAmount());
				financeReportVo.setAccountAmount(amount);
				
				userFinanceReportMap.put(userId, financeReportVo);
			}
		}
		
		List<FinanceReportVo> financeReportVos = new ArrayList<>(userFinanceReportMap.values());
		
		Page<FinanceReportVo> page = new Page<>();
		page.setPageNumber(pageNumber);
		page.setPageSize(pageSize);
		page.setData(financeReportVos);
		page.setTotal(Long.valueOf(filterUser.size()));
		return new Grid<>(page);
	}
	
	@RequestMapping(value = "/sum", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> sum(FinanceReportVo.FinanceReportVoQueryModel financeReportVoQueryModel) {
		
		List<User> users = localCacheComponent.getUsers();
		List<User> filterUser = users.stream().filter(user -> {
			boolean result = true;
			String nicknameLK = financeReportVoQueryModel.getNicknameLK();
			String phoneEQ = financeReportVoQueryModel.getPhoneEQ();
			
			if (!StringUtils.isBlank(nicknameLK)) {
				result = result && StringUtils.contains(user.getNickname(), nicknameLK);
			}
			if (!StringUtils.isBlank(phoneEQ)) {
				result = result && phoneEQ.equals(user.getPhone());
			}
			return result;
		}).collect(Collectors.toList());
		List<Long> userIds = filterUser.stream().map(v -> v.getId()).collect(Collectors.toList());
		if(userIds.isEmpty()) {
			return ResultBuilder.result(null);
		}
		Map<Long, Boolean> userIdMap = userIds.stream().collect(Collectors.toMap(v -> v, v -> true));
		
		List<Payment> payments = localCacheComponent.getPayments();
		List<Deposit> deposits = localCacheComponent.getDeposits();
		List<Withdraw> withdraws = localCacheComponent.getWithdraws();
		List<Profit> profits = localCacheComponent.getProfits();
		List<Account> accounts = localCacheComponent.getAccounts();
		
		List<Payment> filterPayments = payments.stream()
			.filter(payment -> {
				
			boolean result = userIdMap.get(payment.getUserId()) != null;
			
			Date paymentPaidTimeGTE = financeReportVoQueryModel.getTimeGTE();
			Date paymentPaidTimeLT = financeReportVoQueryModel.getTimeLT();
			
			if (paymentPaidTimeGTE != null) {
				result = result && (payment.getPaidTime().after(paymentPaidTimeGTE) || payment.equals(paymentPaidTimeGTE));
			}
			if (paymentPaidTimeLT != null) {
				result = result && payment.getPaidTime().before(paymentPaidTimeLT);
			}
			return result;
		}).collect(Collectors.toList());
		
		List<Deposit> filterDeposits = deposits.stream()
			.filter(deposit -> {
				
			boolean result = userIdMap.get(deposit.getUserId()) != null;
			
			Date depositPaidTimeGTE = financeReportVoQueryModel.getTimeGTE();
			Date depositPaidTimeLT = financeReportVoQueryModel.getTimeLT();
			
			Date paidTime = deposit.getPaidTime();
			if (depositPaidTimeGTE != null) {
				result = result && (paidTime.after(depositPaidTimeGTE) || depositPaidTimeGTE.equals(paidTime));
			}
			if (depositPaidTimeLT != null) {
				result = result && paidTime.before(depositPaidTimeLT);
			}
			return result;
		}).collect(Collectors.toList());
		
		List<Withdraw> filterWithdraws = withdraws.stream()
			.filter(withdraw -> {
				
			boolean result = userIdMap.get(withdraw.getUserId()) != null;
			
			Date withdrawWithdrawedTimeGTE = financeReportVoQueryModel.getTimeGTE();
			Date withdrawWithdrawedTimeLT = financeReportVoQueryModel.getTimeLT();
			
			Date withdrawedTime = withdraw.getWithdrawedTime();
			if (withdrawWithdrawedTimeGTE != null) {
				result = result && (withdrawedTime.after(withdrawWithdrawedTimeGTE) || withdrawWithdrawedTimeGTE.equals(withdrawedTime));
			}
			if (withdrawWithdrawedTimeLT != null) {
				result = result && withdrawedTime.before(withdrawWithdrawedTimeLT);
			}
			return result;
		}).collect(Collectors.toList());
		
		List<Profit> filterProfits = profits.stream()
			.filter(profit -> {
				
			boolean result = userIdMap.get(profit.getUserId()) != null;
			
			Date profitGrantedTimeGTE = financeReportVoQueryModel.getTimeGTE();
			Date profitGrantedTimeLT = financeReportVoQueryModel.getTimeLT();
			
			Date grantedTime = profit.getGrantedTime();
			if (profitGrantedTimeGTE != null) {
				result = result && (grantedTime.after(profitGrantedTimeGTE) || profitGrantedTimeGTE.equals(grantedTime));
			}
			if (profitGrantedTimeLT != null) {
				result = result && grantedTime.before(profitGrantedTimeLT);
			}
			return result;
		}).collect(Collectors.toList());
		
		List<Account> filterAccounts = accounts.stream()
			.filter(account -> {
			return userIdMap.get(account.getUserId()) != null;
		}).collect(Collectors.toList());
		
		BigDecimal zero = new BigDecimal("0.00");
		Map<Long, Boolean> userMap = filterUser.stream().collect(Collectors.toMap(v -> v.getId(), v -> true));
		BigDecimal totalPaymentAmount = zero;
		BigDecimal totalDepositAmount = zero;
		BigDecimal totalWithdrawAmount = zero;
		BigDecimal totalProfitAmount = zero;
		BigDecimal totalAccountAmount = zero;

		for(Payment payment : filterPayments) {
			Long userId = payment.getUserId();
			Boolean status = userMap.get(userId);
			if(status != null) {
				totalPaymentAmount = totalPaymentAmount.add(payment.getAmount1());
			}
		}
		
		for(Deposit deposit : filterDeposits) {
			Long userId = deposit.getUserId();
			Boolean status = userMap.get(userId);
			if(status != null) {
				totalDepositAmount = totalDepositAmount.add(deposit.getAmount1());
			}
		}

		for(Withdraw withdraw : filterWithdraws) {
			Long userId = withdraw.getUserId();
			Boolean status = userMap.get(userId);
			if(status != null) {
				totalWithdrawAmount = totalWithdrawAmount.add(withdraw.getAmount());
			}
		}
		
		for(Profit profit : filterProfits) {
			Long userId = profit.getUserId();
			Boolean status = userMap.get(userId);
			if(status != null) {
				totalProfitAmount = totalProfitAmount.add(profit.getAmount());
			}
		}

		for(Account account : filterAccounts) {
			Long userId = account.getUserId();
			Boolean status = userMap.get(userId);
			if(status != null) {
				totalAccountAmount = totalAccountAmount.add(account.getAmount());
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("totalPaymentAmount", totalPaymentAmount);
		resultMap.put("totalDepositAmount", totalDepositAmount);
		resultMap.put("totalWithdrawAmount", totalWithdrawAmount);
		resultMap.put("totalProfitAmount", totalProfitAmount);
		resultMap.put("totalAccountAmount", totalAccountAmount);
		return ResultBuilder.result(resultMap);
	}
	
	@RequiresPermissions("financeReport:view")
	@RequestMapping(value = "/profit", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> listAjax(@RequestParam Long userId, Date timeGTE, Date timeLT) {
		
		List<Profit> profits = localCacheComponent.getProfits();
		List<Profit> filterProfits = profits.stream().filter(profit -> {
			boolean result = profit.getUserId().equals(userId);
			Date grantedTime = profit.getGrantedTime();
			
			if (timeGTE != null) {
				result = result && (grantedTime.after(timeGTE) || timeGTE.equals(grantedTime));
			}
			if (timeLT != null) {
				result = result && grantedTime.before(timeLT);
			}
			return result;
		}).collect(Collectors.toList());
		
		BigDecimal zero = new BigDecimal("0.00");
		BigDecimal tjpjj = zero;
		BigDecimal xlj = zero;
		BigDecimal sjj = zero;
		BigDecimal ddsk = zero;
		BigDecimal bc = zero;
		for(Profit profit : filterProfits) {
			switch (profit.getProfitType()) {
			case 特级平级奖:
				tjpjj = tjpjj.add(profit.getAmount());
				break;
			case 销量奖:
				xlj = xlj.add(profit.getAmount());
				break;
			case 数据奖:
				sjj = sjj.add(profit.getAmount());
				break;
			case 订单收款:
				ddsk = ddsk.add(profit.getAmount());
				break;
			case 补偿:
				bc = bc.add(profit.getAmount());
				break;
			default:
				break;
			}
		}
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("tjpjj", tjpjj);
		map.put("xlj", xlj);
		map.put("sjj", sjj);
		map.put("ddsk", ddsk);
		map.put("bc", bc);
		map.put("profits", filterProfits.stream().map(profitComponent::buildAdminVo).collect(Collectors.toList()));
		return map;
	}
}
