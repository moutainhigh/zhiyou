package com.zy.component;

import com.zy.common.exception.BizException;
import com.zy.common.model.query.Page;
import com.zy.common.model.tree.TreeHelper;
import com.zy.common.model.tree.TreeNode;
import com.zy.common.model.tree.TreeNodeResolver;
import com.zy.common.util.BeanUtils;
import com.zy.common.util.DateUtil;
import com.zy.entity.sys.SystemCode;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.entity.usr.UserUpgrade;
import com.zy.model.BizCode;
import com.zy.model.TeamModel;
import com.zy.model.dto.DepositSumDto;
import com.zy.model.dto.UserTeamDto;
import com.zy.model.dto.UserDto;
import com.zy.model.query.UserQueryModel;
import com.zy.model.query.UserUpgradeQueryModel;
import com.zy.model.query.UserlongQueryModel;
import com.zy.service.SystemCodeService;
import com.zy.service.UserInfoService;
import com.zy.service.UserService;
import com.zy.service.UserUpgradeService;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class UserComponent {
	
	@Autowired
	private CacheComponent cacheComponent;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private UserUpgradeService userUpgradeService;
	
	@Autowired
	private UserUpgradeComponent userUpgradeComponent;
	
	@Autowired
	private UserInfoComponent userInfoComponent;

	@Autowired
	private SystemCodeService systemCodeService;


	public UserSimpleVo buildSimpleVo(User user) {
		UserSimpleVo userSimpleVo = new UserSimpleVo();
		userSimpleVo.setId(user.getId());
		userSimpleVo.setAvatarThumbnail(GcUtils.getThumbnail(user.getAvatar()));
		userSimpleVo.setNickname(user.getNickname());
		return userSimpleVo;
	}
	
	public UserListVo buildListVo(User user) {
		UserListVo userListVo = new UserListVo();
		userListVo.setId(user.getId());
		userListVo.setAvatarThumbnail(GcUtils.getThumbnail(user.getAvatar()));
		userListVo.setNickname(user.getNickname());
		userListVo.setPhone(user.getPhone());
		userListVo.setUserRank(user.getUserRank());
		userListVo.setIsFrozen(user.getIsFrozen());
		return userListVo;
	}
	
	public UserAdminVo buildAdminVo(User user) {
		UserAdminVo userAdminVo = new UserAdminVo();
		BeanUtils.copyProperties(user, userAdminVo);
		userAdminVo.setAvatarThumbnail(GcUtils.getThumbnail(user.getAvatar()));
		if (user.getInviterId() != null) {
			userAdminVo.setInviter(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(user.getInviterId())));
		}
		if(user.getParentId() != null) {
			userAdminVo.setParent(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(user.getParentId())));
		}
		userAdminVo.setUserRankLabel(GcUtils.getUserRankLabel(user.getUserRank()));
		Long bossId = user.getBossId();
		if (bossId != null) {
			userAdminVo.setBoss(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(bossId)));
		}
		Long presidentId = user.getPresidentId();
		if(presidentId != null){
			userAdminVo.setPresident(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(presidentId)));
		}
		Integer largeArea = user.getLargearea();
		if(largeArea != null){
			SystemCode largeAreaType = systemCodeService.findByTypeAndValue("LargeAreaType", largeArea+"");
			userAdminVo.setLargeareaLabel(largeAreaType.getSystemName());
		}
		return userAdminVo;
	}

	public UserAdminFullVo buildAdminFullVo(User user) {
		UserAdminFullVo userAdminFullVo = new UserAdminFullVo();
		BeanUtils.copyProperties(user, userAdminFullVo);
		
		userAdminFullVo.setUserRankLabel(GcUtils.getUserRankLabel(user.getUserRank()));
		userAdminFullVo.setAvatarThumbnail(GcUtils.getThumbnail(user.getAvatar()));
		Long inviterId = user.getInviterId();
		Long parentId = user.getParentId();
		if(inviterId != null) {
			userAdminFullVo.setInviter(VoHelper.buildUserAdminSimpleVo(userService.findOne(inviterId)));
		}
		if(parentId != null) {
			userAdminFullVo.setParent(VoHelper.buildUserAdminSimpleVo(userService.findOne(parentId)));
		}
		Long userId = user.getId();
		if(userId != null) {
			List<UserUpgrade> userUpgrades = userUpgradeService.findAll(UserUpgradeQueryModel.builder().userIdEQ(userId).build());
			if(!userUpgrades.isEmpty()) {
				userAdminFullVo.setUserUpgrades(userUpgrades.stream().map(userUpgradeComponent::buildAdminVo).collect(Collectors.toList()));
			}
			
			UserInfo userInfo = userInfoService.findByUserId(userId);
			if(userInfo != null) {
				userAdminFullVo.setUserInfo(userInfoComponent.buildAdminVo(userInfo));
			}
		}
		Long presidentId = user.getPresidentId();
		if(presidentId != null){
			userAdminFullVo.setPresident(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(presidentId)));
		}
		
		return userAdminFullVo;
	}
	
	public UserAdminSimpleVo buildAdminSimpleVo(User user) {
		return VoHelper.buildUserAdminSimpleVo(user);
	}

	public UserTeamDto buildUserTeamDto(UserTeamDto userTeamDto){
		userTeamDto.setNickname(userService.findRealName(userTeamDto.getUserId()));
		userTeamDto.setRank(this.getRank(userTeamDto.getUserId()));
		return userTeamDto;
	}

	/**
	 * 获取  用排名
	 * @param userId
	 * @return
     */
	public int getRank(Long userId){
		int myrank=0;
		UserlongQueryModel userlongQueryModel = new UserlongQueryModel();
		userlongQueryModel.setRemark("从V0%");
		userlongQueryModel.setRegisterTimeLT(DateUtil.getBeforeMonthEnd(new Date(),1,0));
		userlongQueryModel.setRegisterTimeGTE(DateUtil.getBeforeMonthBegin(new Date(),0,0));
		List<DepositSumDto> depositSumDtoList = userService.findRankGroup(userlongQueryModel);
		Map<String,Integer> dataMap = new HashMap<String,Integer>();
		if (depositSumDtoList!=null && !depositSumDtoList.isEmpty()){
			for (int i = 0; i < depositSumDtoList.size();i++) { //将list转成map
				DepositSumDto depositSumDto = depositSumDtoList.get(i);
				dataMap.put(String.valueOf(depositSumDto.getSumAmount()),i+1);
			}
			userlongQueryModel.setParentIdNL(userId);
			List<UserTeamDto> mydepositSumDtoList = userService.findByRank(userlongQueryModel);
			if (mydepositSumDtoList != null && !mydepositSumDtoList.isEmpty()) {
				UserTeamDto userTeamDto = mydepositSumDtoList.get(0);
				myrank=dataMap.get(String.valueOf(userTeamDto.getNum()));
			}
		}
		return myrank;
	}

	/**
	 * 计算 活动占比
	 * @param userId
	 * @return
     */
	public String activeProportion(Long userId) {
		UserQueryModel userQueryModel = new UserQueryModel();
		userQueryModel.setRegisterTimeLT(DateUtil.getBeforeMonthEnd(new Date(),1,0));
		userQueryModel.setRegisterTimeGTE(DateUtil.getBeforeMonthBegin(new Date(),-2,0));
		long total = userService.countByActive(userQueryModel);
		userQueryModel.setParentIdNL(userId);
		long mytotal = userService.countByActive(userQueryModel);
		DecimalFormat df = new DecimalFormat("###0.00");
		if (total==0){
			return "0.00";
		}else{
		return df.format((double)mytotal/(double)total*100);
		}
	}

	/**
	 * 统计 直属特级人数
	 * @param userID
	 * @return
     */
	public  List<User> conyteamTotalV4(Long userID) {
		List<User> pageV4Users = new ArrayList<User>();
		UserQueryModel userQueryModel= new UserQueryModel();
		Predicate<User> predicate = v -> {
			User.UserRank userRank = v.getUserRank();
			if (userRank == User.UserRank.V4) {
				return true;
			}
			return false;
		};
		List<User> users = userService.findAll(userQueryModel);
		List<User> v4Users = users.stream().filter(predicate).collect(Collectors.toList());
		Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
		Map<Long, TeamModel> teamMap = v4Users.stream()
				.map(v -> {
					TeamModel teamModel = new TeamModel();
					teamModel.setUser(v);
					teamModel.setV4Children(TreeHelper.sortBreadth2(v4Users, String.valueOf(v.getId()), u -> {
						TreeNode treeNode = new TreeNode();
						treeNode.setId(String.valueOf(u.getId()));
						Long directV4ParentId = getDirectV4ParentId(predicate, userMap, u);
						treeNode.setParentId(u.getParentId() == null ? null : String.valueOf(directV4ParentId));
						return treeNode;

					}));
					teamModel.setDirectV4Children(v4Users.stream().filter(u -> {
						Long userId = u.getId();
						if (userId.equals(v.getId())) {
							return false;
						}
						Long directV4ParentId = getDirectV4ParentId(predicate, userMap, u);
						if (directV4ParentId != null && directV4ParentId.equals(v.getId())) {
							return true;
						}
						return false;
					}).collect(Collectors.toList()));
					return teamModel;
				}).collect(Collectors.toMap(v -> v.getUser().getId(), Function.identity()));
		TeamModel teamModel = teamMap.get(userID);
		List<User> filterV4User=null;
		if(teamModel!=null) {
			filterV4User= teamModel.getDirectV4Children().stream().filter(user -> {
				boolean result = true;
				String nicknameLK = userQueryModel.getNicknameLK();
				String phoneEQ = userQueryModel.getPhoneEQ();

				if (!StringUtils.isBlank(nicknameLK)) {
					result = result && StringUtils.contains(user.getNickname(), nicknameLK);
				}
				if (!StringUtils.isBlank(phoneEQ)) {
					result = result && phoneEQ.equals(user.getPhone());
				}
				return result;
			}).collect(Collectors.toList());
		}
		return filterV4User;
	}

	private Long getDirectV4ParentId(Predicate<User> predicate, Map<Long, User> userMap, User u) {
		int times = 0;
		Long directV4ParentId = null;
		Long parentId = u.getParentId();
		while(parentId != null) {
			if (times > 1000) {
				throw new BizException(BizCode.ERROR, "循环引用");
			}
			User parent = userMap.get(parentId);
			if(parent != null) {
				if (predicate.test(parent)) {
					directV4ParentId = parentId;
					break;
				}
			}
			parentId = parent==null?null:parent.getParentId();
			times ++;
		}
		return directV4ParentId;
	}
	/**
	 * 返回直属特接的ID
	 * @return
     */
	public long[] tId(List<User> filterV4User ){
		if (filterV4User==null||filterV4User.isEmpty()){
			return null;
		}
		long [] ids = new long[filterV4User.size()];
		for (int i=0;i<filterV4User.size();i++){
			ids[i]= filterV4User.get(i).getId();
		}
		return ids;
	}

	/**
	 * 查询 团队的总人数
	 * @param userId
	 * @return
     */
	public long[] conyteamTotal(Long userId) {
		long [] data = new long[]{0,0,0,0};
		List<User> users = userService.findAll(new UserQueryModel());
		List<User> children = TreeHelper.sortBreadth2(users, userId.toString(), v -> {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(v.getId().toString());
			treeNode.setParentId(v.getParentId() == null ? null : v.getParentId().toString());
			return treeNode;
		});
		children = children.stream().filter(v -> v.getUserRank() != User.UserRank.V0).collect(Collectors.toList());
		data[0]= children.stream().filter(v -> v.getUserRank() == User.UserRank.V4).collect(Collectors.toList()).size();
		data[1]= children.stream().filter(v -> v.getUserRank() == User.UserRank.V3).collect(Collectors.toList()).size();
		data[2]= children.stream().filter(v -> v.getUserRank() == User.UserRank.V2).collect(Collectors.toList()).size();
		data[3]= children.stream().filter(v -> v.getUserRank() == User.UserRank.V1).collect(Collectors.toList()).size();
		return data;
	}


	/**
	 * 查看权限
	 * @param user
	 * @return
     */
	public String  findRole(User user) {
		String falg = "F";
		 if (user!=null&&user.getUserRank()==User.UserRank.V4){
			 if (1!=user.getViewflag()){
				 falg ="T";
			 }
		 }
		return  falg;
	}

	/**
	 * 模糊查询   直属特价
	 * @param userId
	 * @param nameorPhone
	 * @param pageNumber
     * @return
     */
	public List<User> conyteamTotalV4page(Long userId, String nameorPhone, Integer pageNumber) {
		List<User> returnList = new ArrayList<User>();
		List<User> users = userService.findAll(new UserQueryModel());
		List<User> children = TreeHelper.sortBreadth2(users, userId.toString(), v -> {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(v.getId().toString());
			treeNode.setParentId(v.getParentId() == null ? null : v.getParentId().toString());
			return treeNode;
		});
		children = children.stream().filter(v -> v.getUserRank() == User.UserRank.V4).collect(Collectors.toList());
		List<User> nameorphoneList = new ArrayList<User>();
		if (children!=null) {
			for (User user :children){
				if (user.getNickname()!=null) {//contains
				    if (user.getPhone()!=null){
						if (user.getNickname().contains(nameorPhone)||user.getPhone().contains(nameorPhone)){
							nameorphoneList.add(user);
						  }
						}else{
						  if (user.getNickname().contains(nameorPhone)){
							  nameorphoneList.add(user);
						 }
					   }
					}else{
							if (user.getPhone().contains(nameorPhone)){
								nameorphoneList.add(user);
							}
					}
			     }
		}
		if (nameorphoneList.size()>(pageNumber*10)+1) {
			for (int i = (pageNumber * 10) + 1; i < (pageNumber * 10) + 1; i++) {
				returnList.add(nameorphoneList.get(i));
			}
		}
		return returnList;
	}


	public List<UserInfoVo> conyteamTotalV4Vo(long userId){
        List<User> userList = this.conyteamTotalV4(userId);
		List<UserInfoVo> dataList = new ArrayList<UserInfoVo>();
		for(User user :userList){
			User newUser = userService.findOne(user.getParentId());
			UserInfoVo userVo = new UserInfoVo();
			userVo.setImage1(user.getAvatar());
			userVo.setId(user.getId());
			userVo.setRealname(user.getNickname());
			userVo.setPhone(user.getPhone());
			userVo.setPname(newUser.getNickname());
			userVo.setPphone(newUser.getPhone());
			//判断是不是 新晋成员
			if (userService.findNewOne(user.getId())) {
				userVo.setNewflag("T");
			}
			dataList.add(userVo);
		}
		return dataList;
	}


	/**
	 * 查询  所有 用户 信息
	 * @param userQueryModel
	 * @return
     */
	public Page<UserDto> findUserAll(UserQueryModel userQueryModel) {
		List<UserDto> userList = userService.findUserAll(userQueryModel);
		long total = userService.countUserAll(userQueryModel);
		Page<UserDto> page = new Page<>();
		page.setPageNumber(userQueryModel.getPageNumber());
		page.setPageSize(userQueryModel.getPageSize());
		page.setData(userList);
		page.setTotal(total);
		return page;
	}

	public  List<User> sortBreadthV3(Collection<User> entities, String parentId) {
		List<User> result = new ArrayList<>();
		Map<String, List<User>> childrenMap = entities.stream().collect(Collectors.groupingBy(v -> v.getParentId() == null ? "DEFAULT_NULL_KEY" :v.getParentId().toString()));
		sortBreadthV3(childrenMap, result, parentId);
		return result;
	}

	private  void sortBreadthV3(Map<String, List<User>> childrenMap, Collection<User> result, String parentId) {
		parentId = parentId == null ? "DEFAULT_NULL_KEY" : parentId;
		List<User> plist = childrenMap.get(parentId);
		if (plist == null) {
			return;
		}
		plist = plist.stream().filter(u -> u.getUserRank()== User.UserRank.V3).collect(Collectors.toList());
		result.addAll(plist);
		for (User user : plist) {
			sortBreadthV3(childrenMap, result, user.getId().toString());
		}
	}
}
