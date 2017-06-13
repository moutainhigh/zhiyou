package com.zy.mobile.controller.ucenter;

import com.zy.common.util.DateUtil;
import com.zy.component.UserComponent;
import com.zy.entity.usr.Address;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.model.Principal;
import com.zy.model.query.UserQueryModel;
import com.zy.service.AddressService;
import com.zy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import java.util.stream.Collectors;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@RequestMapping("/u/team")
@Controller
public class UcenterTeamController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private UserComponent userComponent;

	@RequestMapping
	public String agent(Principal principal, Model model) {
		Long userId = principal.getUserId();
		UserQueryModel userQueryModel = new UserQueryModel();
		userQueryModel.setParentIdEQ(userId);
		List<User> agents = userService.findAll(userQueryModel);
		agents = agents.stream().filter(v -> v.getUserRank() != UserRank.V0).collect(Collectors.toList());
		model.addAttribute("list", agents.stream().map(userComponent::buildListVo).collect(Collectors.toList()));
		
		long agentsCount = agents.size();
		long lv3AgentsCount = agentsCount;
		if(agentsCount > 0l){
			Set<Long> parentIdSet = new HashSet<>();
			parentIdSet.add(userId);
			parentIdSet.addAll(agents.stream().map(User::getId).collect(Collectors.toSet()));
			userQueryModel.setParentIdEQ(null);
			userQueryModel.setParentIdIN(parentIdSet.toArray(new Long[]{}));
			List<User> lv2Agents = userService.findAll(userQueryModel);
			parentIdSet.addAll(lv2Agents.stream().map(User::getId).collect(Collectors.toSet()));
			userQueryModel.setParentIdIN(parentIdSet.toArray(new Long[]{}));
			lv3AgentsCount = userService.count(userQueryModel);
		}
		model.addAttribute("agentsCount", agentsCount);
		model.addAttribute("allAgentsCount", lv3AgentsCount);
		return "ucenter/team/userList";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable Long id, Long level, Principal principal, Model model){
		if(level == null){
			level = 1L;
		}
		Long principalUserId = principal.getUserId();
		validate(id, NOT_NULL, "user id is null");
		User user = userService.findOne(id);
		validate(user, NOT_NULL, "user id " + id + "is not found");
		model.addAttribute("user", userComponent.buildListVo(user));
		model.addAttribute("principalUserId", principalUserId);
		
		if(user.getParentId() != null && !principalUserId.equals(user.getParentId())){
			User parentLv1 = userService.findOne(user.getParentId());
			if(parentLv1.getUserRank() != UserRank.V0){
				model.addAttribute("parentLv1", userComponent.buildListVo(parentLv1));
			}
			if(parentLv1.getParentId() != null && !principalUserId.equals(parentLv1.getParentId())){
				User parentLv2 = userService.findOne(parentLv1.getParentId());
				if(parentLv2.getUserRank() != UserRank.V0){
					model.addAttribute("parentLv2", userComponent.buildListVo(parentLv2));
				}
			}
		}
		
		Address address = addressService.findDefaultByUserId(id);
		model.addAttribute("address", address);
		
		UserQueryModel userQueryModel = new UserQueryModel();
		userQueryModel.setParentIdEQ(id);
		List<User> agents = userService.findAll(userQueryModel);
		agents = agents.stream().filter(v -> v.getUserRank() != UserRank.V0).collect(Collectors.toList());
		model.addAttribute("list", agents.stream().map(userComponent::buildListVo).collect(Collectors.toList()));
		
		model.addAttribute("level", level);
		return "ucenter/team/userDetail";
	}

	/**
	 * 我的团队新的  业务逻辑
	 * @param principal
	 * @param model
     * @return
     */
	@RequestMapping(value = "/newTeam", method = RequestMethod.GET)
	public String  newTeam(Principal principal, Model model){
		Long userId = principal.getUserId();
        Map<String,Object> dataMap = new HashMap<String,Object>();
		//统计团队人数
		long[]teamTotal = userService.conyteamTotal(userId);
		dataMap.put("TTot", DateUtil.longarryToString(teamTotal,false));
		//直属团队 人数统计
		long [] dirTotal = userService.countdirTotal(userId);
		dataMap.put("DTot", DateUtil.longarryToString(dirTotal,false));
        //统计团队新成员
		 Map<String,Object>map =userService.countNewMemTotal(userId,false);
		long [] newMem = (long[])map.get("MTot");
		dataMap.put("MTot", DateUtil.longarryToString(newMem,false));
		dataMap.put("pro",DateUtil.countPro((long[])map.get("MTot"),(long)map.get("total")));
        //处理排名

		Map<String,Object>rankMap = userService.disposeRank(userId,false);
		return null;
	}




}
