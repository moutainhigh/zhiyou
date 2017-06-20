package com.zy.mobile.controller.ucenter;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.util.DateUtil;
import com.zy.component.UserComponent;
import com.zy.entity.usr.Address;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.model.Principal;
import com.zy.model.dto.UserTeamDto;
import com.zy.model.query.UserQueryModel;
import com.zy.model.query.UserlongQueryModel;
import com.zy.service.AddressService;
import com.zy.service.UserService;
import com.zy.vo.UserInfoVo;
import io.gd.generator.api.query.Direction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
		User user = userService.findOne(userId);
		if (user.getUserRank()==User.UserRank.V4||user.getUserRank()==User.UserRank.V3){
			long[]teamTotal=userComponent.conyteamTotal(userId);
			/*long[]teamTotal = userService.conyteamTotal(userId);*/
			dataMap.put("TTot", DateUtil.longarryToString(teamTotal,false));
			dataMap.put("flag", userComponent.findRole(user));//是否 能查看到 直属特级按钮
		}
		//直属团队 人数统计
		long [] dirTotal = userService.countdirTotal(userId);
		/*long [] dirTotal = userComponent.countdirTotal(userId);*/
		dataMap.put("DTot", DateUtil.longarryToString(dirTotal,false));//直属团队成员
        //统计团队新成员
		 Map<String,Object>map =userService.countNewMemTotal(userId,true);
		long [] newMem = (long[])map.get("MTot");//新增人员  数据
		dataMap.put("MTot", DateUtil.longarryToString(newMem,false));
		dataMap.put("Pro",DateUtil.countPro((long[])map.get("MTot"),(long)map.get("total")));//新增人员占比
        //处理排名
		UserlongQueryModel userlongQueryModel = new UserlongQueryModel();
		userlongQueryModel.setParentIdNL(userId);
		userlongQueryModel.setPageNumber(0);
		userlongQueryModel.setPageSize(5);
		Page<UserTeamDto> page= userService.disposeRank(userlongQueryModel,false);
		Page<UserTeamDto> voPage = PageBuilder.copyAndConvert(page, v-> userComponent.buildUserTeamDto(v));
		dataMap.put("rankList",voPage.getData());//排名数据
		dataMap.put("myRank",userComponent.getRank(userId));//我的排序
        //处理新进特级
        long ids[]=userComponent.tId(userComponent.conyteamTotalV4(userId));
        dataMap.put("myTids", DateUtil.longarryToString(dirTotal,false));//将直属特级 存下来
        Map<String,Object> newSup = userService.findNewSup(ids);
        dataMap.put("mynT",newSup.get("MY"));//直属特级*/
		dataMap.put("actPer",userComponent.activeProportion(userId)); //活跃占比
		UserQueryModel userQueryModel = new UserQueryModel();
		userQueryModel.setParentIdNL(userId);
		userQueryModel.setPageNumber(0);
		userQueryModel.setPageSize(5);
		Page<User> pageact= userService.findActive(userQueryModel,false);
		dataMap.put("act",pageact.getData());//不活跃人员
		model.addAttribute("dataMap",dataMap);
		return "ucenter/teamNew/userListNew";
	}

	/**
	 * 查看直属特级详情
	 * @return
     */
	@RequestMapping(value = "findDirectlySup")
	public String  findDirectlySup(Principal principal, Model model){
	    List<UserInfoVo> userList= userComponent.conyteamTotalV4Vo(principal.getUserId());
		model.addAttribute("data",userList);
		return "ucenter/teamNew/mustDetil";
	}
	/**
	 * 查看直属特级详情
	 * @return
	 */
	/*@RequestMapping(value = "ajaxfindDirectlySup")
	public String ajaxfindDirectlySup(Principal principal,String nameorPhone,@RequestParam(required = true) Integer pageNumber){
		List<User> userList= userComponent.conyteamTotalV4page(principal.getUserId(),nameorPhone,pageNumber);//模拟分页查询
		return null;
	}*/
	/**
	 *  跳转到直属团队详情 页面
	 */
	@RequestMapping(value = "teamDetail")
	public String  teamDetail(Principal principal, Model model){
		Long userId = principal.getUserId();
		UserQueryModel userQueryModel = new UserQueryModel();
		userQueryModel.setParentIdEQ(userId);
		Page<User> page= userService.findPage(userQueryModel);
		/*model.addAttribute("page",page);*/
		model.addAttribute("v4",page.getData().stream().filter(v -> v.getUserRank() == UserRank.V4).collect(Collectors.toList()));
		model.addAttribute("v3",page.getData().stream().filter(v -> v.getUserRank() == UserRank.V3).collect(Collectors.toList()));
		model.addAttribute("v2",page.getData().stream().filter(v -> v.getUserRank() == UserRank.V2).collect(Collectors.toList()));
		model.addAttribute("v1",page.getData().stream().filter(v -> v.getUserRank() == UserRank.V1).collect(Collectors.toList()));
		model.addAttribute("v0",page.getData().stream().filter(v -> v.getUserRank() == UserRank.V0).collect(Collectors.toList()));
		return "ucenter/teamNew/teamDetil";
	}

	/**
	 * 异步加载  直属团队
	 */
	@RequestMapping(value = "ajaxTeamDetail",method = RequestMethod.POST)
	@ResponseBody
	public Result<?> ajaxTeamDetail(Principal principal,String nameorPhone,@RequestParam(required = true) Integer pageNumber){
		Long userId = principal.getUserId();
		UserQueryModel userQueryModel = new UserQueryModel();
		userQueryModel.setParentIdEQ(userId);
		userQueryModel.setDirection(Direction.DESC);
		userQueryModel.setOrderBy("user_rank");
		if (null!=nameorPhone){
			userQueryModel.setNameorPhone("%"+nameorPhone+"%");
		}

		userQueryModel.setPageNumber(pageNumber);
		userQueryModel.setPageSize(10);
		Page<User> page= userService.findPage(userQueryModel);
		return ResultBuilder.result(page.getData());
	}

	/**
	 * 查询沉睡成员
	 */
	@RequestMapping(value = "teamSleep")
	public String teamSleep(Principal principal, Model model){
		Long userId = principal.getUserId();
		UserQueryModel userQueryModel = new UserQueryModel();
		userQueryModel.setParentIdNL(userId);
        Page<User> page = userService.findActive(userQueryModel,true);
		model.addAttribute("v4",page.getData().stream().filter(v -> v.getUserRank() == UserRank.V4).collect(Collectors.toList()));
		model.addAttribute("v3",page.getData().stream().filter(v -> v.getUserRank() == UserRank.V3).collect(Collectors.toList()));
		model.addAttribute("v2",page.getData().stream().filter(v -> v.getUserRank() == UserRank.V2).collect(Collectors.toList()));
		model.addAttribute("v1",page.getData().stream().filter(v -> v.getUserRank() == UserRank.V1).collect(Collectors.toList()));
		model.addAttribute("v0",page.getData().stream().filter(v -> v.getUserRank() == UserRank.V0).collect(Collectors.toList()));
		return "";//TODo
	}

	/**
	 * 动态加载 团队 沉睡成员
	 * @param principal
	 * @param nameorPhone
	 * @param pageNumber
     * @return
     */
	@RequestMapping(value = "ajaxTeamSleep",method = RequestMethod.POST)
	@ResponseBody
	public  Result<?> ajaxTeamSleep(Principal principal,String nameorPhone, @RequestParam(required = true) Integer pageNumber){
		Long userId = principal.getUserId();
		UserQueryModel userQueryModel = new UserQueryModel();
		userQueryModel.setParentIdNL(userId);
		if (null!=nameorPhone){
			userQueryModel.setNameorPhone("%"+nameorPhone+"%");
		}
		userQueryModel.setPageNumber(pageNumber);
		userQueryModel.setPageSize(10);
		Page<User> page = userService.findActive(userQueryModel,true);
		return ResultBuilder.result(page.getData());
	}

	/**
	 * 处理 活跃排序数据插叙
	 * @param principal
	 * @param model
     * @return
     */
	@RequestMapping(value = "teamRank")
	public String teamRank(Principal principal, Model model){
		Long userId = principal.getUserId();
		UserlongQueryModel userlongQueryModel= new UserlongQueryModel();
		userlongQueryModel.setParentIdNL(userId);
		userlongQueryModel.setPageNumber(0);
		userlongQueryModel.setPageSize(10);
		Page<UserTeamDto> page = userService.disposeRank(userlongQueryModel,true);
		model.addAttribute("page",page);
        return null;
	}

	/**
	 * 动态加载  排名数据
	 * @param principal
	 * @param nameorPhone
	 * @param pageNumber
     * @return
     */
	@RequestMapping(value = "ajaxteamRank",method = RequestMethod.POST)
	@ResponseBody
	public  Result<?> ajaxteamRank(Principal principal,String nameorPhone, @RequestParam(required = true) Integer pageNumber){
		Long userId = principal.getUserId();
		UserlongQueryModel userlongQueryModel= new UserlongQueryModel();
		userlongQueryModel.setParentIdNL(userId);
		userlongQueryModel.setPageNumber(pageNumber);
		userlongQueryModel.setPageSize(10);
		if (null!=nameorPhone){
			userlongQueryModel.setNameorPhone(nameorPhone);
		}
		Page<UserTeamDto> page = userService.disposeRank(userlongQueryModel,true);
		return ResultBuilder.result(page.getData());
	}

	/**
	 *团队 新成员
	 * @return
     */
	@RequestMapping(value = "teamNew")
	public String teamNew(Principal principal,Model model){
		Long userId = principal.getUserId();
		UserQueryModel userQueryModel = new UserQueryModel();
		userQueryModel.setParentIdNL(userId);
		Page<User> page =userService.findAddpeople(userQueryModel);
		model.addAttribute("v4",page.getData().stream().filter(v -> v.getUserRank() == UserRank.V4).collect(Collectors.toList()));
		model.addAttribute("v3",page.getData().stream().filter(v -> v.getUserRank() == UserRank.V3).collect(Collectors.toList()));
		model.addAttribute("v2",page.getData().stream().filter(v -> v.getUserRank() == UserRank.V2).collect(Collectors.toList()));
		model.addAttribute("v1",page.getData().stream().filter(v -> v.getUserRank() == UserRank.V1).collect(Collectors.toList()));
		model.addAttribute("v0",page.getData().stream().filter(v -> v.getUserRank() == UserRank.V0).collect(Collectors.toList()));
		return "ucenter/teamNew/newTeamDetil";
	}

	/**
	 * 动态加载  团队新成员
	 * @param principal
	 * @param nameorPhone
	 * @param pageNumber
	 * @return
	 */
	@RequestMapping(value = "ajaxteamNew",method = RequestMethod.POST)
	@ResponseBody
	public  Result<?> ajaxteamNew(Principal principal,String nameorPhone, @RequestParam(required = true) Integer pageNumber){
		Long userId = principal.getUserId();
		UserQueryModel userQueryModel = new UserQueryModel();
		userQueryModel.setParentIdNL(userId);
		userQueryModel.setPageNumber(pageNumber);
		userQueryModel.setPageSize(10);
		if (null!=nameorPhone){
			userQueryModel.setNameorPhone(nameorPhone);
		}
		Page<User> page =userService.findAddpeople(userQueryModel);
		return ResultBuilder.result(page.getData());
	}

	/**
	 * 查询 直属 团队的人数
	 * @param userId
	 * @return
     */
	public String findDirectlyNum(Long userId, Model model){
		long [] dirTotal = userService.countdirTotal(userId);
		return null;//ResultBuilder.result(dirTotal);
	}

	/**
	 *查询  直属特级的团队人数
	 */
	@RequestMapping(value = "ajaxfindDirectlySup",method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> findDirectlySupNum(Long userId, Model model){
		long[]teamTotal=userComponent.conyteamTotal(userId);
		String result= DateUtil.longarryToString(teamTotal,false);
		 return ResultBuilder.ok(result);
	}
}
