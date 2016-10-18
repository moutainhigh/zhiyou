package com.zy.component;

import com.zy.common.exception.BizException;
import com.zy.common.util.BeanUtils;
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
import java.util.stream.Collectors;

@Component
public class LocalCacheComponent {

    final Logger logger = LoggerFactory.getLogger(LocalCacheComponent.class);

    private Map<Long, OrderItem> orderItemMap = new HashMap<>();

    private List<User> users = new ArrayList<>();

    private List<Order> orders = new ArrayList<>();

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
    private OrderItemService orderItemService;

    @Autowired
    private UserService userService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private UserUpgradeService userUpgradeService;

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

    public void refresh() {
        logger.info("refresh begin...");
	    List<Area> newAreas = areaService.findAll(new AreaQueryModel());
	    Map<Long, Area> newAreaMap = newAreas.stream().collect(Collectors.toMap(v -> v.getId(), v -> v));

        List<User> newUsers = userService.findAll(UserQueryModel.builder().userTypeEQ(User.UserType.代理).build());
        List<String> newRootNames = newUsers.stream().filter(v -> v.getIsRoot()).map(User::getRootName).distinct().collect(Collectors.toList());
        List<Order> newOrders = orderService.findAll(OrderQueryModel.builder().build());
        Map<Long, OrderItem> newOrderItemMap = orderItemService.findAll().stream().collect(Collectors.toMap(v -> v.getOrderId(), v -> v));
	    Map<Long, User> newUserMap = newUsers.stream().collect(Collectors.toMap(v -> v.getId(), v -> v));
	    Map<Long, UserInfo> newUserInfoMap = userInfoService.findAll(UserInfoQueryModel.builder().build()).stream().collect(Collectors.toMap(v -> v.getUserId(), v -> v));
	    List<UserUpgrade> newUserUpgrades = userUpgradeService.findAll(UserUpgradeQueryModel.builder().build());

	    areas = newAreas;
	    areaMap = newAreaMap;
        users = newUsers;
        orders = newOrders;
        orderItemMap = newOrderItemMap;
	    userMap = newUserMap;
	    userInfoMap = newUserInfoMap;
	    userUpgrades = newUserUpgrades;
	    rootNames = newRootNames; 
	    
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
			    if (parent.getUserRank() == User.UserRank.V4) {
				    v4UserId =  tmpParentId;
			    }
			    tmpParentId = parent.getParentId();
			    whileTimes ++;
		    }
		    if (v4UserId != null) {
			    User v4User = newUserMap.get(v4UserId);
			    userReportVo.setV4UserId(v4UserId);
			    userReportVo.setV4UserNickname(v4User.getNickname());
		    }

		    Long rootId = null;
		    if (user.getIsRoot()) {
			    rootId = userId;
		    } else {
			    tmpParentId = parentId;
			    whileTimes = 0;
			    while (tmpParentId != null) {
				    if (whileTimes > 1000) {
					    throw new BizException(BizCode.ERROR, "循环引用错误, user id is " + user.getId());
				    }
				    User parent =  newUserMap.get(tmpParentId);
				    if (parent.getIsRoot() != null && parent.getIsRoot()) {
					    rootId = tmpParentId;
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

        logger.info("refresh end...");

    }

    @PostConstruct
    public void init() {
        refresh();
    }




}
