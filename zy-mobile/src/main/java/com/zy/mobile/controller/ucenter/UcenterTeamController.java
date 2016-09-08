package com.zy.mobile.controller.ucenter;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zy.component.UserComponent;
import com.zy.entity.usr.Address;
import com.zy.entity.usr.User;
import com.zy.model.Principal;
import com.zy.model.query.UserQueryModel;
import com.zy.service.AddressService;
import com.zy.service.UserService;

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
		userQueryModel.setInviterIdEQ(userId);
		List<User> agents = userService.findAll(userQueryModel);
		model.addAttribute("list", agents.stream().map(userComponent::buildListVo).collect(Collectors.toList()));
		
		long agentsCount = agents.size();
		long allAgentsCount = agentsCount;
		if(agentsCount > 0l){
			Set<Long> inviterIdSet = new HashSet<>();
			inviterIdSet.add(userId);
			inviterIdSet.addAll(agents.stream().map(User::getId).collect(Collectors.toSet()));
			userQueryModel.setInviterIdEQ(null);
			userQueryModel.setInviterIdIN(inviterIdSet.toArray(new Long[]{}));
			allAgentsCount = userService.count(userQueryModel);
		}
		model.addAttribute("agentsCount", agentsCount);
		model.addAttribute("allAgentsCount", allAgentsCount);
		return "ucenter/team/userList";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable Long id, Principal principal, Model model){
		validate(id, NOT_NULL, "user id is null");
		User user = userService.findOne(id);
		validate(user, NOT_NULL, "user id " + id + "is not found");
		model.addAttribute("user", userComponent.buildListVo(user));
		model.addAttribute("principalUserId", principal.getUserId());
		
		if(user.getInviterId() != null){
			User inviterLv1 = userService.findOne(user.getInviterId());
			model.addAttribute("inviterLv1", userComponent.buildListVo(inviterLv1));
			if(inviterLv1.getInviterId() != null){
				User inviterLv2 = userService.findOne(inviterLv1.getInviterId());
				model.addAttribute("inviterLv2", userComponent.buildListVo(inviterLv2));
				if(inviterLv2.getInviterId() != null){
					User inviterLv3 = userService.findOne(inviterLv2.getInviterId());
					model.addAttribute("inviterLv3", userComponent.buildListVo(inviterLv3));
				}
			}
		}
		
		Address address = addressService.findDefaultByUserId(id);
		model.addAttribute("address", address);
		
		UserQueryModel userQueryModel = new UserQueryModel();
		userQueryModel.setInviterIdEQ(id);
		List<User> agents = userService.findAll(userQueryModel);
		model.addAttribute("list", agents.stream().map(userComponent::buildListVo).collect(Collectors.toList()));
		return "ucenter/team/userDetail";
	}
	
}
