package com.zy.component;

import com.zy.common.exception.BizException;
import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.*;
import com.zy.entity.fnc.Deposit.DepositStatus;
import com.zy.entity.fnc.Payment.PaymentStatus;
import com.zy.entity.fnc.Profit.ProfitStatus;
import com.zy.entity.fnc.Withdraw.WithdrawStatus;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.OrderItem;
import com.zy.entity.sys.Area;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.entity.usr.UserUpgrade;
import com.zy.model.BizCode;
import com.zy.model.query.*;
import com.zy.service.*;
import com.zy.vo.UserReportVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.zy.component.ThreadPool.counter;
import static com.zy.component.ThreadPool.executorService;


public class LocalCacheComponent {

	final Logger logger = LoggerFactory.getLogger(LocalCacheComponent.class);

	private Map<Long, OrderItem> orderItemMap = new HashMap<>();

	private List<User> users = new ArrayList<>();

	private List<Order> orders = new ArrayList<>();

	private List<Payment> payments = new ArrayList<>();

	private List<Deposit> deposits = new ArrayList<>();

	private List<Withdraw> withdraws = new ArrayList<>();

	private List<Profit> profits = new ArrayList<>();

	private List<Transfer> transfers = new ArrayList<>();

	private List<Account> accounts = new ArrayList<>();

	private Map<Long, User> userMap = new HashMap<>();

	private Map<Long, UserInfo> userInfoMap = new HashMap<>();

	private List<UserReportVo> userReportVos = new ArrayList<>();

	private List<String> rootNames = new ArrayList<String>();

	private List<Area> areas = new ArrayList<>();

	private Map<Long, Area> areaMap = new HashMap<>();

	private List<UserUpgrade> userUpgrades = new ArrayList<>();

	@Autowired
	private OrderService orderService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private DepositService depositService;

	@Autowired
	private WithdrawService withdrawService;

	@Autowired
	private ProfitService profitService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private UserService userService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private TransferService transferService;

	@Autowired
	private UserUpgradeService userUpgradeService;

	public List<Area> getAreas() {
		return areas;
	}

	public List<User> getUsers() {
		return users;
	}

	public Map<Long, User> getUserMap() {
		return userMap;
	}

	public Map<Long, UserInfo> getUserInfoMap() {
		return userInfoMap;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public List<Payment> getPayments() {
		return payments;
	}

	public List<Deposit> getDeposits() {
		return deposits;
	}

	public List<Withdraw> getWithdraws() {
		return withdraws;
	}

	public List<Profit> getProfits() {
		return profits;
	}

	public List<Transfer> getTransfers() {
		return transfers;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public Map<Long, OrderItem> getOrderItemMap() {
		return orderItemMap;
	}

	public List<UserReportVo> getuserReportVos() {
		return userReportVos;
	}

	public List<String> getRootNames() {
		return rootNames;
	}

	public List<UserUpgrade> getUserUpgrades() {
		return userUpgrades;
	}

	public void refresh() throws InterruptedException {
		CountDownLatch downLatch = new CountDownLatch(13);
		CountDownLatch userReportVoContunDownLatch = new CountDownLatch(2);


		logger.info("refresh begin...");
		List<Area> newAreas = new ArrayList<>();
		List<User> newUsers = new ArrayList<>();
		Map<Long, User> newUserMap = new HashMap<>(256);
		Map<Long, Area> newAreaMap = new HashMap<>(64);
		List<String> newRootNames = new ArrayList<>();
		List<Order> newOrders = new ArrayList<>();
		List<Payment> newPayments = new ArrayList<>(3000*10);
		List<Deposit> newDeposits = new ArrayList<>(3000*10);
		List<Withdraw> newWithdraws = new ArrayList<>(3000*10);
		List<Profit> newProfits = new ArrayList<>(2000*10);
		List<Transfer> newTransfer = new ArrayList<>(1000*10);
		List<Account> newAccounts = new ArrayList<>(1000*10);
		Map<Long, OrderItem> newOrderItemMap = new HashMap<>(256);
		Map<Long, UserInfo> newUserInfoMap = new HashMap<>(1000*10);
		List<UserUpgrade> newUserUpgrades = new ArrayList<>(1000*10);

		executorService.execute(() -> {
			newAreas.addAll(areaService.findAll(new AreaQueryModel()));
			newAreaMap.putAll(newAreas.stream().collect(Collectors.toMap(v -> v.getId(), v -> v)));
			downLatch.countDown();
			userReportVoContunDownLatch.countDown();
		});
		executorService.execute(() -> {
			newUsers.addAll(userService.findAll(UserQueryModel.builder().userTypeEQ(User.UserType.代理).build()));
			newRootNames.addAll(newUsers.stream().filter(v -> v.getIsRoot() != null && v.getIsRoot()).map(User::getRootName).distinct().collect(Collectors.toList()));
			newUserMap.putAll(newUsers.stream().collect(Collectors.toMap(v -> v.getId(), v -> v)));
			downLatch.countDown();
			userReportVoContunDownLatch.countDown();
		});
		executorService.execute(() -> {
			newOrders.addAll(orderService.findAll(OrderQueryModel.builder().build()));
			downLatch.countDown();
		});
		executorService.execute(() -> {
			newPayments.addAll(paymentService.findAll(PaymentQueryModel.builder().paymentStatusEQ(PaymentStatus.已支付).build()));
			downLatch.countDown();
		});
		executorService.execute(() -> {
			newDeposits.addAll(depositService.findAll(DepositQueryModel.builder().depositStatusEQ(DepositStatus.充值成功).build()));
			downLatch.countDown();
		});
		executorService.execute(() -> {
			newWithdraws.addAll(withdrawService.findAll(WithdrawQueryModel.builder().withdrawStatusEQ(WithdrawStatus.提现成功).build()));
			downLatch.countDown();
		});
		executorService.execute(() -> {
			newProfits.addAll(profitService.findAll(ProfitQueryModel.builder().profitStatusEQ(ProfitStatus.已发放).build()));
			downLatch.countDown();
		});
		executorService.execute(() -> {
			newTransfer.addAll(transferService.findAll(TransferQueryModel.builder().transferTypeEQ(Transfer.TransferType.U币转账).transferStatusEQ(Transfer.TransferStatus.已转账).build()));
			downLatch.countDown();
		});
		executorService.execute(() -> {
			newAccounts.addAll(accountService.findAll(AccountQueryModel.builder().build()));
			downLatch.countDown();
		});
		executorService.execute(() -> {
			newOrderItemMap.putAll(orderItemService.findAll().stream().collect(Collectors.toMap(v -> v.getOrderId(), v -> v)));
			downLatch.countDown();
		});
		executorService.execute(() -> {
			newUserInfoMap.putAll(userInfoService.findAll(UserInfoQueryModel.builder().build()).stream().collect(Collectors.toMap(v -> v.getUserId(), v -> v)));
			downLatch.countDown();
		});
		executorService.execute(() -> {
			newUserUpgrades.addAll(userUpgradeService.findAll(UserUpgradeQueryModel.builder().build()));
			downLatch.countDown();
		});

		executorService.execute(()->{
			try {
				userReportVoContunDownLatch.await();
			} catch (InterruptedException e) {
				logger.error(e.getMessage(),e);
			}
			userReportVos = newUsers.stream().map(user -> {
				UserReportVo userReportVo = new UserReportVo();
				BeanUtils.copyProperties(user, userReportVo);
				Long userId = user.getId();
				UserInfo userInfo = newUserInfoMap.get(userId);
				if (userInfo != null) {
					Long areaId = userInfo.getAreaId();
					if (areaId != null) {
						Long districtId = areaId;
						Area district = areaMap.get(districtId);
						if (district != null && district.getAreaType() == Area.AreaType.区) {
							userReportVo.setDistrictId(districtId);
							Long cityId = district.getParentId();
							Area city = areaMap.get(cityId);
							if (city != null && city.getAreaType() == Area.AreaType.市) {
								userReportVo.setCityId(cityId);
								Long provinceId = city.getParentId();
								Area province = areaMap.get(provinceId);
								if (province != null && province.getAreaType() == Area.AreaType.省) {
									userReportVo.setProvinceId(provinceId);
								}
							}

						}
					}
				}

				Long parentId = user.getParentId();
				int whileTimes = 0;

				Long v4UserId = null;
				Long tmpParentId = parentId;
				while (tmpParentId != null) {
					if (whileTimes > 1000) {
						throw new BizException(BizCode.ERROR, "循环引用错误, user id is " + userId);
					}
					User parent = newUserMap.get(tmpParentId);
					if (parent == null) {
						throw new BizException(BizCode.ERROR, "关联了错误的parent id " + tmpParentId);
					}
					if (parent.getUserRank() == User.UserRank.V4) {
						v4UserId = tmpParentId;
						break;
					}
					tmpParentId = parent.getParentId();
					whileTimes++;
				}
				if (v4UserId != null) {
					User v4User = newUserMap.get(v4UserId);
					userReportVo.setV4UserId(v4UserId);
					userReportVo.setV4UserNickname(v4User.getNickname());
				}

				Long rootId = null;
				if (user.getIsRoot() != null && user.getIsRoot()) {
					rootId = userId;
				} else {
					tmpParentId = parentId;
					whileTimes = 0;
					while (tmpParentId != null) {
						if (whileTimes > 1000) {
							throw new BizException(BizCode.ERROR, "循环引用错误, user id is " + user.getId());
						}
						User parent = newUserMap.get(tmpParentId);
						if (parent.getIsRoot() != null && parent.getIsRoot()) {
							rootId = tmpParentId;
							break;
						}
						tmpParentId = parent.getParentId();
						whileTimes++;
					}
				}
				if (rootId != null) {
					User root = newUserMap.get(rootId);
					userReportVo.setRootId(rootId);
					userReportVo.setRootRootName(root.getRootName());
				}
				return userReportVo;

			}).collect(Collectors.toList());
			downLatch.countDown();
		});


		downLatch.await();
		areas = newAreas;
		areaMap = newAreaMap;
		users = newUsers;
		orders = newOrders;
		payments = newPayments;
		deposits = newDeposits;
		withdraws = newWithdraws;
		profits = newProfits;
		transfers = newTransfer;
		accounts = newAccounts;
		orderItemMap = newOrderItemMap;
		userMap = newUserMap;
		userInfoMap = newUserInfoMap;
		userUpgrades = newUserUpgrades;
		rootNames = newRootNames;
		logger.info("refresh end...");

	}

	@PostConstruct
	public void init() throws InterruptedException {
		refresh();
	}
}

class ThreadPool {
	static final AtomicInteger counter = new AtomicInteger(0);
	public final static ExecutorService executorService = Executors.newFixedThreadPool(13,r -> {
		Thread thread = new Thread(r);
		thread.setName(String.format("local-cache-component-refresh-%s-thread",counter.getAndIncrement()));
		thread.setDaemon(true);
		return thread;
	});

}

